package com.easyFit.gui.back.equipement;

import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.entities.Fournisseur;
import com.easyFit.services.FournisseurService;
import com.easyFit.utils.Statics;

import java.util.ArrayList;

public class ChooseFournisseur extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");
    Form previousForm;
    Button addBtn;
    Label nomFLabel, prenomFLabel, telFLabel, emailFLabel, adresseLabel, ribFLabel;
    ImageViewer imageIV;
    Button chooseBtn;
    Container btnsContainer;

    public ChooseFournisseur(Form previous) {
        super("Choisir un fournisseur", new BoxLayout(BoxLayout.Y_AXIS));

        previousForm = previous;
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
        addBtn = new Button("Ajouter fournisseur");
        this.add(addBtn);

        ArrayList<Fournisseur> listFournisseurs = FournisseurService.getInstance().getAll();
        if (listFournisseurs.size() > 0) {
            for (Fournisseur fournisseurs : listFournisseurs) {
                this.add(makeFournisseurModel(fournisseurs));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> new com.easyFit.gui.back.fournisseur.Manage(this, true).show());
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

        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            Manage.selectedFournisseur = fournisseur;
            ((Manage) previousForm).refreshFournisseur();
            previousForm.showBack();
        });

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        fournisseurModel.addAll(
                imageIV, nomFLabel, prenomFLabel, telFLabel, emailFLabel, adresseLabel, ribFLabel,
                btnsContainer
        );

        return fournisseurModel;
    }
}