package com.easyFit.gui.back.abonne;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.entities.Abonne;
import com.easyFit.services.AbonneService;
import com.easyFit.utils.Statics;

import java.util.ArrayList;

public class DisplayAll extends Form {


    public static Abonne currentAbonne = null;
    Resources theme = UIManager.initFirstTheme("/theme");
    Button addBtn;
    Label nomLabel, prenomLabel, ageLabel, sexeLabel, emailLabel, mdpLabel, telLabel, adresseLabel, abonnementLabel, messageLabel, imageLabel;
    ImageViewer imageIV;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Abonnes", new BoxLayout(BoxLayout.Y_AXIS));

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

        ArrayList<Abonne> listAbonnes = AbonneService.getInstance().getAll();
        if (listAbonnes.size() > 0) {
            for (Abonne listAbonne : listAbonnes) {
                this.add(makeAbonneModel(listAbonne));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentAbonne = null;
            new Manage(this).show();
        });
    }

    private Component makeAbonneModel(Abonne abonne) {
        Container abonneModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        abonneModel.setUIID("containerRounded");


        nomLabel = new Label("Nom : " + abonne.getNom());

        nomLabel.setUIID("labelDefault");


        prenomLabel = new Label("Prenom : " + abonne.getPrenom());

        prenomLabel.setUIID("labelDefault");


        ageLabel = new Label("Age : " + abonne.getAge());

        ageLabel.setUIID("labelDefault");


        sexeLabel = new Label("Sexe : " + abonne.getSexe());

        sexeLabel.setUIID("labelDefault");


        emailLabel = new Label("Email : " + abonne.getEmail());

        emailLabel.setUIID("labelDefault");


        mdpLabel = new Label("Mdp : " + abonne.getMdp());

        mdpLabel.setUIID("labelDefault");


        telLabel = new Label("Tel : " + abonne.getTel());

        telLabel.setUIID("labelDefault");


        adresseLabel = new Label("Adresse : " + abonne.getAdresse());

        adresseLabel.setUIID("labelDefault");


        abonnementLabel = new Label("Abonnement : " + abonne.getAbonnement());

        abonnementLabel.setUIID("labelDefault");


        messageLabel = new Label("Message : " + abonne.getMessage());

        messageLabel.setUIID("labelDefault");


        imageLabel = new Label("Image : " + abonne.getImage());

        imageLabel.setUIID("labelDefault");


        abonnementLabel = new Label("Abonnement : " + abonne.getAbonnement().getType());
        abonnementLabel.setUIID("labelDefault");


        if (abonne.getImage() != null) {
            String url = Statics.ABONNE_IMAGE_URL + abonne.getImage();
            Image image = URLImage.createToStorage(
                    EncodedImage.createFromImage(theme.getImage("default.jpg").fill(500, 500), false),
                    url,
                    url,
                    URLImage.RESIZE_SCALE
            );
            imageIV = new ImageViewer(image);
        } else {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(500, 500));
        }
        imageIV.setFocusable(false);


        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonMain");
        editBtn.addActionListener(action -> {
            currentAbonne = abonne;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce abonne ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = AbonneService.getInstance().delete(abonne.getId());

                if (responseCode == 200) {
                    currentAbonne = null;
                    dlg.dispose();
                    abonneModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du abonne. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.CENTER, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        abonneModel.addAll(
                imageIV,
                nomLabel, prenomLabel, ageLabel,
                sexeLabel, emailLabel, mdpLabel,
                telLabel, adresseLabel, abonnementLabel,
                messageLabel, btnsContainer
        );

        return abonneModel;
    }
}