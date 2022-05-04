package com.easyFit.gui.back.salle;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.entities.Salle;
import com.easyFit.services.SalleService;
import com.easyFit.utils.Statics;

import java.util.ArrayList;

public class DisplayAll extends Form {


    public static Salle currentSalle = null;
    Resources theme = UIManager.initFirstTheme("/theme");
    Button addBtn;
    Label nomLabel, adresseLabel, codePostalLabel, villeLabel, nombreLabel, imageLabel, longitudeLabel, lattitudeLabel;
    ImageViewer imageIV;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Salles", new BoxLayout(BoxLayout.Y_AXIS));

        addGUIs();
        addActions();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        addActions();
        this.refreshTheme();
    }

    private void addGUIs() {
        addBtn = new Button("Ajouter");
        addBtn.setUIID("buttonWhiteCenter");

        this.add(addBtn);

        ArrayList<Salle> listSalles = SalleService.getInstance().getAll();
        if (listSalles.size() > 0) {
            for (Salle listSalle : listSalles) {
                this.add(makeSalleModel(listSalle));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentSalle = null;
            new Manage(this).show();
        });
    }

    private Component makeSalleModel(Salle salle) {
        Container salleModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        salleModel.setUIID("containerRounded");


        nomLabel = new Label("Nom : " + salle.getNom());

        nomLabel.setUIID("labelDefault");


        adresseLabel = new Label("Adresse : " + salle.getAdresse());

        adresseLabel.setUIID("labelDefault");


        codePostalLabel = new Label("CodePostal : " + salle.getCodePostal());

        codePostalLabel.setUIID("labelDefault");


        villeLabel = new Label("Ville : " + salle.getVille());

        villeLabel.setUIID("labelDefault");


        nombreLabel = new Label("Nombre : " + salle.getNombre());

        nombreLabel.setUIID("labelDefault");


        imageLabel = new Label("Image : " + salle.getImage());

        imageLabel.setUIID("labelDefault");


        longitudeLabel = new Label("Longitude : " + salle.getLongitude());

        longitudeLabel.setUIID("labelDefault");


        lattitudeLabel = new Label("Lattitude : " + salle.getLattitude());

        lattitudeLabel.setUIID("labelDefault");


        if (salle.getImage() != null) {
            String url = Statics.SALLE_IMAGE_URL + salle.getImage();
            Image image = URLImage.createToStorage(
                    EncodedImage.createFromImage(theme.getImage("default.jpg").fill(700, 500), false),
                    url,
                    url,
                    URLImage.RESIZE_SCALE
            );
            imageIV = new ImageViewer(image);
        } else {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(700, 500));
        }
        imageIV.setFocusable(false);


        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonMain");
        editBtn.addActionListener(action -> {
            currentSalle = salle;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce salle ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = SalleService.getInstance().delete(salle.getId());

                if (responseCode == 200) {
                    currentSalle = null;
                    dlg.dispose();
                    salleModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du salle. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.CENTER, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        salleModel.addAll(
                imageIV,

                nomLabel, adresseLabel, codePostalLabel, villeLabel, nombreLabel, longitudeLabel, lattitudeLabel,

                btnsContainer
        );

        return salleModel;
    }
}