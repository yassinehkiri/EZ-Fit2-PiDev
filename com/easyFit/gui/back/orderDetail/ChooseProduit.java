package com.easyFit.gui.back.orderDetail;

import com.codename1.components.ImageViewer;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.entities.Produit;
import com.easyFit.services.ProduitService;
import com.easyFit.utils.Statics;

import java.util.ArrayList;

public class ChooseProduit extends Form {

    Form previousForm;
    Resources theme = UIManager.initFirstTheme("/theme");

    Label descriptionLabel, nomPLabel, nombreLabel, prixLabel, reductionLabel, categorieLabel;
    SpanLabel descriptionSpanLabel;
    ImageViewer imageIV;

    Button chooseBtn;
    Container btnsContainer;

    public ChooseProduit(Form previous) {
        super("Choisir un produit", new BoxLayout(BoxLayout.Y_AXIS));

        previousForm = previous;
        addGUIs();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        this.refreshTheme();
    }

    private void addGUIs() {
        ArrayList<Produit> listProduits = ProduitService.getInstance().getAll();
        if (listProduits.size() > 0) {
            for (Produit produits : listProduits) {
                this.add(makeProduitModel(produits));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private Component makeProduitModel(Produit produit) {
        Container produitModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        produitModel.setUIID("containerRounded");

        nomPLabel = new Label(produit.getNomP());
        nomPLabel.setUIID("labelCenter");

        if (produit.getImage() != null) {
            String url = Statics.POST_IMAGE_URL + produit.getImage();
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

        descriptionLabel = new Label("Description : " + produit.getDescription());
        descriptionLabel.setUIID("labelDefault");

        descriptionSpanLabel = new SpanLabel(produit.getDescription());

        nombreLabel = new Label("Nombre : " + produit.getNombre());
        nombreLabel.setUIID("labelDefault");

        prixLabel = new Label("Prix : " + produit.getPrix());
        prixLabel.setUIID("labelDefault");

        reductionLabel = new Label("Reduction : " + produit.getReduction());
        reductionLabel.setUIID("labelDefault");

        if (produit.getCategorie() != null) {
            categorieLabel = new Label("Categorie : " + produit.getCategorie().getNomC());
        } else {
            categorieLabel = new Label("Categorie : null");
        }
        categorieLabel.setUIID("labelDefault");

        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            Manage.selectedProduit = produit;
            ((Manage) previousForm).refreshProduit();
            previousForm.showBack();
        });

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        produitModel.addAll(
                nomPLabel,
                imageIV,
                descriptionLabel, descriptionSpanLabel,
                nombreLabel, prixLabel, reductionLabel,
                categorieLabel,
                btnsContainer
        );

        return produitModel;
    }
}