package com.easyFit.gui.back.reclamation;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.entities.Reclamation;
import com.easyFit.services.ReclamationService;
import com.easyFit.utils.Statics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {


    public static Reclamation currentReclamation = null;
    Resources theme = UIManager.initFirstTheme("/theme");
    Label redacteurLabel, dateLabel, contenuLabel, imageLabel;
    ImageViewer imageIV;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Reclamations", new BoxLayout(BoxLayout.Y_AXIS));

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
        ArrayList<Reclamation> listReclamations = ReclamationService.getInstance().getAll();
        if (listReclamations.size() > 0) {
            for (Reclamation listReclamation : listReclamations) {
                this.add(makeReclamationModel(listReclamation));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
     
    }

    private Component makeReclamationModel(Reclamation reclamation) {
        Container reclamationModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        reclamationModel.setUIID("containerRounded");


        redacteurLabel = new Label("Redacteur : " + reclamation.getRedacteur());

        redacteurLabel.setUIID("labelDefault");


        dateLabel = new Label("Date : " + new SimpleDateFormat("dd-MM-yyyy").format(reclamation.getDate()));

        dateLabel.setUIID("labelDefault");


        contenuLabel = new Label("Contenu : " + reclamation.getContenu());

        contenuLabel.setUIID("labelDefault");


        imageLabel = new Label("Image : " + reclamation.getImage());

        imageLabel.setUIID("labelDefault");


        if (reclamation.getImage() != null) {
            String url = Statics.RECLAMATION_IMAGE_URL + reclamation.getImage();
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
            currentReclamation = reclamation;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce reclamation ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = ReclamationService.getInstance().delete(reclamation.getId());

                if (responseCode == 200) {
                    currentReclamation = null;
                    dlg.dispose();
                    reclamationModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du reclamation. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.CENTER, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        reclamationModel.addAll(
                redacteurLabel, dateLabel, contenuLabel, imageLabel,

                imageIV,


                btnsContainer
        );

        return reclamationModel;
    }
}