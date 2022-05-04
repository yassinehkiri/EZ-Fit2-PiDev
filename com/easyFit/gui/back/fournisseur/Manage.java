package com.easyFit.gui.back.fournisseur;

import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.entities.Fournisseur;
import com.easyFit.gui.back.equipement.ChooseFournisseur;
import com.easyFit.services.FournisseurService;
import com.easyFit.utils.Statics;

import java.io.IOException;

public class Manage extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");

    boolean imageEdited = false;
    boolean isChoose;

    Fournisseur currentFournisseur;
    String selectedImage;

    Label nomFLabel, prenomFLabel, telFLabel, emailFLabel, adresseLabel, ribFLabel, imageLabel;
    TextField nomFTF, prenomFTF, telFTF, emailFTF, adresseTF, ribFTF;
    ImageViewer imageIV;
    Button selectImageButton, manageButton;

    Form previous;

    public Manage(Form previous, boolean isChoose) {
        super(DisplayAll.currentFournisseur == null ? "Ajouter fournisseur" : "Modifier fournisseur", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;
        this.isChoose = isChoose;

        currentFournisseur = DisplayAll.currentFournisseur;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        nomFLabel = new Label("Nom : ");
        nomFLabel.setUIID("labelDefault");
        nomFTF = new TextField();
        nomFTF.setHint("Tapez le nom du fournisseur");

        prenomFLabel = new Label("Prenom : ");
        prenomFLabel.setUIID("labelDefault");
        prenomFTF = new TextField();
        prenomFTF.setHint("Tapez le prenom du fournisseur");

        telFLabel = new Label("Tel : ");
        telFLabel.setUIID("labelDefault");
        telFTF = new TextField();
        telFTF.setHint("Tapez le tel du fournisseur");

        emailFLabel = new Label("Email : ");
        emailFLabel.setUIID("labelDefault");
        emailFTF = new TextField();
        emailFTF.setHint("Tapez le email du fournisseur");

        adresseLabel = new Label("Addresse : ");
        adresseLabel.setUIID("labelDefault");
        adresseTF = new TextField();
        adresseTF.setHint("Tapez le addresse du fournisseur");

        ribFLabel = new Label("Rib : ");
        ribFLabel.setUIID("labelDefault");
        ribFTF = new TextField();
        ribFTF.setHint("Tapez le rib du fournisseur");

        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        if (currentFournisseur == null) {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(500, 500));
            manageButton = new Button("Ajouter");
        } else {
            nomFTF.setText(currentFournisseur.getNomF());
            prenomFTF.setText(currentFournisseur.getPrenomF());
            telFTF.setText(String.valueOf(currentFournisseur.getTelF()));
            emailFTF.setText(currentFournisseur.getEmailF());
            adresseTF.setText(currentFournisseur.getAdresse());
            ribFTF.setText(currentFournisseur.getRibF());

            if (currentFournisseur.getImage() != null) {
                String url = Statics.FOURNISSEUR_IMAGE_URL + currentFournisseur.getImage();
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
            selectedImage = currentFournisseur.getImage();

            manageButton = new Button("Modifier");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                nomFLabel, nomFTF,
                prenomFLabel, prenomFTF,
                telFLabel, telFTF,
                emailFLabel, emailFTF,
                adresseLabel, adresseTF,
                ribFLabel, ribFTF,
                imageLabel, imageIV, selectImageButton,
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

        if (currentFournisseur == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = FournisseurService.getInstance().add(
                            new Fournisseur(
                                    nomFTF.getText(),
                                    prenomFTF.getText(),
                                    (int) Float.parseFloat(telFTF.getText()),
                                    emailFTF.getText(),
                                    adresseTF.getText(),
                                    ribFTF.getText(),
                                    selectedImage
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Fournisseur ajouté avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de fournisseur. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = FournisseurService.getInstance().edit(
                            new Fournisseur(
                                    currentFournisseur.getId(),
                                    nomFTF.getText(),
                                    prenomFTF.getText(),
                                    (int) Float.parseFloat(telFTF.getText()),
                                    emailFTF.getText(),
                                    adresseTF.getText(),
                                    ribFTF.getText(),
                                    selectedImage
                            ),
                            imageEdited
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Fournisseur modifié avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de fournisseur. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        }
    }

    private void showBackAndRefresh() {
        if (isChoose) {
            ((ChooseFournisseur) previous).refresh();
        } else {
            ((DisplayAll) previous).refresh();
        }
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (nomFTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nom", new Command("Ok"));
            return false;
        }

        if (prenomFTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le prenom", new Command("Ok"));
            return false;
        }

        if (telFTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le tel", new Command("Ok"));
            return false;
        }
        if (telFTF.getText().length() != 8) {
            Dialog.show("Avertissement", "Tel doit avoir 8 chiffres", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(telFTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", telFTF.getText() + " n'est pas un tel valide", new Command("Ok"));
            return false;
        }

        if (emailFTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir l'email", new Command("Ok"));
            return false;
        }

        if (adresseTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir l'addresse", new Command("Ok"));
            return false;
        }

        if (ribFTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le rib", new Command("Ok"));
            return false;
        }

        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }
        return true;
    }
}