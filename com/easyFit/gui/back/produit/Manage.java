package com.easyFit.gui.back.produit;

import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.entities.Categorie;
import com.easyFit.entities.Produit;
import com.easyFit.services.ProduitService;
import com.easyFit.utils.Statics;

import java.io.IOException;

public class Manage extends Form {

    public static Categorie selectedCategorie;
    Resources theme = UIManager.initFirstTheme("/theme");
    boolean imageEdited = false;

    Produit currentProduit;
    String selectedImage;

    Label descriptionLabel, nomPLabel, nombreLabel, prixLabel, reductionLabel, imageLabel, categorieLabel, selectedCategorieLabel;
    TextField descriptionTF, nomPTF, nombreTF, prixTF, reductionTF;
    ImageViewer imageIV;

    Button selectCategorieButton, selectImageButton, manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentProduit == null ? "Nouveau produit" : "Modifier produit", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentProduit = DisplayAll.currentProduit;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refreshCategorie() {
        selectedCategorieLabel.setText(selectedCategorie.getNomC());
        selectCategorieButton.setText("Choisir  categorie");
        this.refreshTheme();
    }

    private void addGUIs() {

        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");
        descriptionTF = new TextField();
        descriptionTF.setHint("Tapez un nombre");

        nomPLabel = new Label("Nom : ");
        nomPLabel.setUIID("labelDefault");
        nomPTF = new TextField();
        nomPTF.setHint("Tapeez le nom du produit");

        nombreLabel = new Label("Nombre : ");
        nombreLabel.setUIID("labelDefault");
        nombreTF = new TextField();
        nombreTF.setHint("Tapez un nombre");

        prixLabel = new Label("Prix : ");
        prixLabel.setUIID("labelDefault");
        prixTF = new TextField();
        prixTF.setHint("Exp: 50");

        reductionLabel = new Label("Reduction : ");
        reductionLabel.setUIID("labelDefault");
        reductionTF = new TextField();
        reductionTF.setHint("Exp: 10");

        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        if (currentProduit == null) {
            selectedCategorie = null;
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(500, 500));
            manageButton = new Button("Ajouter");
        } else {
            descriptionTF.setText(String.valueOf(currentProduit.getDescription()));
            nomPTF.setText(currentProduit.getNomP());
            nombreTF.setText(String.valueOf(currentProduit.getNombre()));
            prixTF.setText(String.valueOf(currentProduit.getPrix()));
            reductionTF.setText(String.valueOf(currentProduit.getReduction()));
            selectedCategorie = currentProduit.getCategorie();

            if (currentProduit.getImage() != null) {
                String url = Statics.PRODUIT_IMAGE_URL + currentProduit.getImage();
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

            selectImageButton.setText("Choisir  l'image");
            selectedImage = currentProduit.getImage();

            manageButton = new Button("Modifier");
        }

        categorieLabel = new Label("Categorie : ");
        categorieLabel.setUIID("labelDefault");
        if (selectedCategorie != null) {
            selectedCategorieLabel = new Label(selectedCategorie.getNomC());
            selectCategorieButton = new Button("Modifier categorie");
        } else {
            selectedCategorieLabel = new Label("Aucune categorie selectionné");
            selectCategorieButton = new Button("Choisir une categorie");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                nomPLabel, nomPTF,
                descriptionLabel, descriptionTF,
                nombreLabel, nombreTF,
                prixLabel, prixTF,
                reductionLabel, reductionTF,
                imageLabel, imageIV, selectImageButton,
                categorieLabel, selectedCategorieLabel, selectCategorieButton,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
        selectImageButton.addActionListener(a -> {
            selectedImage = Capture.capturePhoto(900, -1);
            try {
                imageEdited = true;
                imageIV.setImage(Image.createImage(selectedImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            selectImageButton.setText("Choisir  l'image");
        });

        selectCategorieButton.addActionListener(l -> new ChooseCategorie(this).show());

        if (currentProduit == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = ProduitService.getInstance().add(
                            new Produit(
                                    descriptionTF.getText(),
                                    nomPTF.getText(),
                                    (int) Float.parseFloat(nombreTF.getText()),
                                    Float.parseFloat(prixTF.getText()),
                                    Float.parseFloat(reductionTF.getText()),
                                    selectedCategorie,
                                    selectedImage
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Produit ajouté avec succés", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de produit. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    ((DisplayAll) previous).refresh();
                    previous.showBack();
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = ProduitService.getInstance().edit(
                            new Produit(
                                    currentProduit.getId(),
                                    descriptionTF.getText(),
                                    nomPTF.getText(),
                                    (int) Float.parseFloat(nombreTF.getText()),
                                    Float.parseFloat(prixTF.getText()),
                                    Float.parseFloat(reductionTF.getText()),
                                    selectedCategorie,
                                    selectedImage
                            ),
                            imageEdited
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Produit modifié avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de produit. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    ((DisplayAll) previous).refresh();
                    previous.showBack();
                }
            });
        }
    }

    private boolean controleDeSaisie() {
        if (descriptionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir la description", new Command("Ok"));
            return false;
        }

        if (nomPTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nom", new Command("Ok"));
            return false;
        }

        if (nombreTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nombre", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(nombreTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", nombreTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }

        if (prixTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le prix", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(prixTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", prixTF.getText() + " n'est pas un prix valide", new Command("Ok"));
            return false;
        }

        if (reductionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir la reduction", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(reductionTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", reductionTF.getText() + " n'est pas une reduction valide", new Command("Ok"));
            return false;
        }

        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }

        if (selectedCategorie == null) {
            Dialog.show("Avertissement", "Veuillez choisir une categorie", new Command("Ok"));
            return false;
        }

        return true;
    }
}