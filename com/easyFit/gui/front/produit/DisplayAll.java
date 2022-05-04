package com.easyFit.gui.front.produit;

import com.codename1.components.ImageViewer;
import com.codename1.components.ShareButton;
import com.codename1.components.SpanLabel;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.ImageIO;
import com.codename1.ui.util.Resources;
import com.easyFit.entities.Produit;
import com.easyFit.gui.front.AccueilFront;
import com.easyFit.services.ProduitService;
import com.easyFit.utils.Statics;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class DisplayAll extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");
    Label descriptionLabel, nomPLabel, nombreLabel, prixLabel, reductionLabel, categorieLabel;
    SpanLabel descriptionSpanLabel;
    ImageViewer imageIV;

    Container btnsContainer;
    TextField searchTF;
    ArrayList<Component> componentModels;
    public DisplayAll() {
        super("Produits", new BoxLayout(BoxLayout.Y_AXIS));

        addGUIs();

        super.getToolbar().hideToolbar();
    }

    private void addGUIs() {
        ArrayList<Produit> listProduits = ProduitService.getInstance().getAll();
        componentModels = new ArrayList<>();

        searchTF = new TextField("", "Chercher un produit par nom");
        searchTF.addDataChangedListener((d, t) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    this.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            for (Produit produit : listProduits) {
                if (produit.getNomP().startsWith(searchTF.getText())) {
                    Component model = makeProduitModel(produit);
                    this.add(model);
                    componentModels.add(model);
                }
            }
            this.revalidate();
        });
        this.add(searchTF);

        if (listProduits.size() > 0) {
            for (Produit produit : listProduits) {
                Component model = makeProduitModel(produit);
                this.add(model);
                componentModels.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private Component makeProduitModel(Produit produit) {
        Container produitModel = makeModelWithoutButtons(produit);

        /*buyButton.addActionListener(l -> {
            new Manage(AccueilFront.accueilFrontForm).show();
        });*/

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

        descriptionLabel = new Label("Description : ");
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

        produitModel.addAll(
                nomPLabel,
                imageIV,
                descriptionLabel, descriptionSpanLabel,
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
        screenShotForm.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> AccueilFront.accueilFrontForm.showBack());
        screenShotForm.show();
        // FIN API PARTAGE
    }
}