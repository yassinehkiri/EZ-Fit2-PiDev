package com.easyFit.gui.back.fournisseur;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.entities.Fournisseur;
import com.easyFit.services.FournisseurService;
import com.easyFit.utils.Statics;

import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Fournisseur currentFournisseur = null;
    Resources theme = UIManager.initFirstTheme("/theme");
    Button addBtn;
    Label nomFLabel, prenomFLabel, telFLabel, emailFLabel, adresseLabel, ribFLabel;
    ImageViewer imageIV;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Fournisseurs", new BoxLayout(BoxLayout.Y_AXIS));

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

        this.add(addBtn);

        ArrayList<Fournisseur> listFournisseurs = FournisseurService.getInstance().getAll();
        if (listFournisseurs.size() > 0) {
            for (Fournisseur listFournisseur : listFournisseurs) {
                this.add(makeFournisseurModel(listFournisseur));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentFournisseur = null;
            new Manage(this, false).show();
        });
    }

    private Component makeFournisseurModel(Fournisseur fournisseur) {
        Container fournisseurModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        fournisseurModel.setUIID("containerRounded");

        nomFLabel = new Label("Nom : " + fournisseur.getNomF());
        nomFLabel.setUIID("labelDefault");

        prenomFLabel = new Label("Prenom : " + fournisseur.getPrenomF());
        prenomFLabel.setUIID("labelDefault");

        telFLabel = new Label("Tel : " + fournisseur.getTelF());
        telFLabel.setUIID("labelDefault");

        emailFLabel = new Label("Email : " + fournisseur.getEmailF());
        emailFLabel.setUIID("labelDefault");

        adresseLabel = new Label("Adresse : " + fournisseur.getAdresse());
        adresseLabel.setUIID("labelDefault");

        ribFLabel = new Label("Rib : " + fournisseur.getRibF());
        ribFLabel.setUIID("labelDefault");

        if (fournisseur.getImage() != null) {
            String url = Statics.FOURNISSEUR_IMAGE_URL + fournisseur.getImage();
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
        editBtn.addActionListener(action -> {
            currentFournisseur = fournisseur;
            new Manage(this, false).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce fournisseur ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = FournisseurService.getInstance().delete(fournisseur.getId());

                if (responseCode == 200) {
                    currentFournisseur = null;
                    dlg.dispose();
                    fournisseurModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du fournisseur. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        fournisseurModel.addAll(
                imageIV, nomFLabel, prenomFLabel, telFLabel, emailFLabel, adresseLabel, ribFLabel,
                btnsContainer
        );

        return fournisseurModel;
    }
}