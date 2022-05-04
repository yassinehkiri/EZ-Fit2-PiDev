package com.easyFit.gui.front.categorie;

import com.codename1.components.ImageViewer;
import com.codename1.components.ShareButton;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.ImageIO;
import com.codename1.ui.util.Resources;
import com.easyFit.entities.Produit;
import com.easyFit.services.ProduitService;
import com.easyFit.utils.Statics;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class DisplayProdByCat extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");
    int idCategorie;
    Label descriptionLabel, nomPLabel, nombreLabel, prixLabel, reductionLabel, categorieLabel;
    ImageViewer imageIV;
    Container btnsContainer;

    public DisplayProdByCat(Form previous, int idCategorie) {
        super("Produits", new BoxLayout(BoxLayout.Y_AXIS));
        this.idCategorie = idCategorie;

        addGUIs();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {
        ArrayList<Produit> listProduits = ProduitService.getInstance().getByCat(idCategorie);
        if (listProduits.size() > 0) {
            for (Produit listProduit : listProduits) {
                this.add(makeProduitModel(listProduit));
            }
        } else {
            this.add(new Label("Aucun produit"));
        }
    }

    private Component makeProduitModel(Produit produit) {
        Container produitModel = makeModelWithoutButtons(produit);

        Button btnAfficherScreenshot = new Button("Partager");
        btnAfficherScreenshot.addActionListener(listener -> share(produit));

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.add(BorderLayout.CENTER, btnAfficherScreenshot);


        produitModel.add(btnsContainer);
        return produitModel;
    }

    private Container makeModelWithoutButtons(Produit produit) {
        Container produitModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        produitModel.setUIID("containerRounded");

        nomPLabel = new Label(produit.getNomP());
        nomPLabel.setUIID("labelCenter");

        if (produit.getImage() != null) {
            String url = Statics.PRODUIT_IMAGE_URL + produit.getImage();
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

        nombreLabel = new Label("Nombre : " + produit.getNombre());
        nombreLabel.setUIID("labelDefault");

        prixLabel = new Label("Prix : " + produit.getPrix());
        prixLabel.setUIID("labelDefault");

        reductionLabel = new Label("Reduction : " + produit.getReduction());
        reductionLabel.setUIID("labelDefault");

        categorieLabel = new Label("Categorie : " + produit.getCategorie().getNomC());
        categorieLabel.setUIID("labelDefault");

        produitModel.addAll(
                nomPLabel,
                imageIV, descriptionLabel,
                nombreLabel, prixLabel, reductionLabel,
                categorieLabel
        );

        return produitModel;
    }

    private void share(Produit produit) {
        Form form = new Form();
        form.add(new Label("Produit " + produit.getNomP()));
        form.add(makeModelWithoutButtons(produit));
        String imageFile = FileSystemStorage.getInstance().getAppHomePath() + "screenshot.png";
        Image screenshot = Image.createImage(
                Display.getInstance().getDisplayWidth(),
                Display.getInstance().getDisplayHeight()
        );
        form.revalidate();
        form.setVisible(true);
        form.paintComponent(screenshot.getGraphics(), true);
        form.removeAll();
        try (OutputStream os = FileSystemStorage.getInstance().openOutputStream(imageFile)) {
            ImageIO.getImageIO().save(screenshot, os, ImageIO.FORMAT_PNG, 1);
        } catch (IOException err) {
            Log.e(err);
        }
        Form screenShotForm = new Form("Partager le produit", new BoxLayout(BoxLayout.Y_AXIS));
        ImageViewer screenshotViewer = new ImageViewer(screenshot.fill(1000, 2000));
        screenshotViewer.setFocusable(false);
        screenshotViewer.setUIID("screenshot");
        ShareButton btnPartager = new ShareButton();
        btnPartager.setText("Partager ");
        btnPartager.setTextPosition(LEFT);
        btnPartager.setImageToShare(imageFile, "image/png");
        btnPartager.setTextToShare(produit.toString());
        screenShotForm.addAll(screenshotViewer, btnPartager);
        screenShotForm.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> this.showBack());
        screenShotForm.show();
        // FIN API PARTAGE
    }
}