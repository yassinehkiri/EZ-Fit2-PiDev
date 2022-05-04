package com.easyFit.gui.back.abonne;


import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.entities.Abonne;
import com.easyFit.entities.Abonnement;
import com.easyFit.services.AbonneService;
import com.easyFit.utils.Statics;

import java.io.IOException;

public class Manage extends Form {


    public static Abonnement selectedAbonnement;
    Resources theme = UIManager.initFirstTheme("/theme");
    String selectedImage;
    boolean imageEdited = false;
    Abonne currentAbonne;
    Label nomLabel, prenomLabel, ageLabel, sexeLabel, emailLabel, mdpLabel, telLabel, adresseLabel, abonnementLabel, messageLabel, imageLabel;
    TextField
            nomTF,
            prenomTF,
            ageTF,
            sexeTF,
            emailTF,
            mdpTF,
            telTF,
            adresseTF,
            abonnementTF,
            messageTF,
            imageTF, elemTF;


    Label selectedAbonnementLabel;
    Button selectAbonnementButton;


    ImageViewer imageIV;
    Button selectImageButton;

    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentAbonne == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;


        selectedAbonnement = null;

        currentAbonne = DisplayAll.currentAbonne;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }


    public void refreshAbonnement() {
        selectedAbonnementLabel.setText(selectedAbonnement.getType());
        selectAbonnementButton.setText("Choisir  abonnement");
        this.refreshTheme();
    }


    private void addGUIs() {


        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");

        prenomLabel = new Label("Prenom : ");
        prenomLabel.setUIID("labelDefault");
        prenomTF = new TextField();
        prenomTF.setHint("Tapez le prenom");

        ageLabel = new Label("Age : ");
        ageLabel.setUIID("labelDefault");
        ageTF = new TextField();
        ageTF.setHint("Tapez le age");

        sexeLabel = new Label("Sexe : ");
        sexeLabel.setUIID("labelDefault");
        sexeTF = new TextField();
        sexeTF.setHint("Tapez le sexe");

        emailLabel = new Label("Email : ");
        emailLabel.setUIID("labelDefault");
        emailTF = new TextField();
        emailTF.setHint("Tapez le email");

        mdpLabel = new Label("Mdp : ");
        mdpLabel.setUIID("labelDefault");
        mdpTF = new TextField();
        mdpTF.setHint("Tapez le mdp");

        telLabel = new Label("Tel : ");
        telLabel.setUIID("labelDefault");
        telTF = new TextField();
        telTF.setHint("Tapez le tel");

        adresseLabel = new Label("Adresse : ");
        adresseLabel.setUIID("labelDefault");
        adresseTF = new TextField();
        adresseTF.setHint("Tapez le adresse");

        abonnementLabel = new Label("Abonnement : ");
        abonnementLabel.setUIID("labelDefault");
        abonnementTF = new TextField();
        abonnementTF.setHint("Tapez le abonnement");

        messageLabel = new Label("Message : ");
        messageLabel.setUIID("labelDefault");
        messageTF = new TextField();
        messageTF.setHint("Tapez le message");


        abonnementLabel = new Label("abonnement : ");
        abonnementLabel.setUIID("labelDefault");
        selectedAbonnementLabel = new Label("null");
        selectAbonnementButton = new Button("Choisir abonnement");
        selectAbonnementButton.addActionListener(l -> {
            new ChooseAbonnement(this).show();
        });


        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        if (currentAbonne == null) {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(500, 500));
            manageButton = new Button("Ajouter");
        } else {
            nomTF.setText(currentAbonne.getNom());
            prenomTF.setText(currentAbonne.getPrenom());
            ageTF.setText(String.valueOf(currentAbonne.getAge()));
            sexeTF.setText(currentAbonne.getSexe());
            emailTF.setText(currentAbonne.getEmail());
            mdpTF.setText(currentAbonne.getMdp());
            telTF.setText(currentAbonne.getTel());
            adresseTF.setText(currentAbonne.getAdresse());
            messageTF.setText(currentAbonne.getMessage());
            selectedAbonnement = currentAbonne.getAbonnement();
            abonnementLabel = new Label("abonnement : ");
            abonnementLabel.setUIID("labelDefault");
            selectedAbonnementLabel = new Label("null");
            selectedAbonnementLabel.setText(selectedAbonnement.getType());
            selectAbonnementButton.setText("Choisir  abonnement");
            selectedImage = currentAbonne.getImage();

            if (currentAbonne.getImage() != null) {
                String url = Statics.ABONNE_IMAGE_URL + currentAbonne.getImage();
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


            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonMain");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(

                imageLabel, imageIV,
                selectImageButton,
                nomLabel, nomTF,
                prenomLabel, prenomTF,
                ageLabel, ageTF,
                sexeLabel, sexeTF,
                emailLabel, emailTF,
                mdpLabel, mdpTF,
                telLabel, telTF,
                adresseLabel, adresseTF,
                messageLabel, messageTF,
                abonnementLabel,
                selectedAbonnementLabel, selectAbonnementButton,
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

        if (currentAbonne == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = AbonneService.getInstance().add(
                            new Abonne(


                                    nomTF.getText(),
                                    prenomTF.getText(),
                                    (int) Float.parseFloat(ageTF.getText()),
                                    sexeTF.getText(),
                                    emailTF.getText(),
                                    mdpTF.getText(),
                                    telTF.getText(),
                                    adresseTF.getText(),
                                    selectedAbonnement,
                                    messageTF.getText(),
                                    selectedImage
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Abonne ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de abonne. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = AbonneService.getInstance().edit(
                            new Abonne(
                                    currentAbonne.getId(),


                                    nomTF.getText(),
                                    prenomTF.getText(),
                                    (int) Float.parseFloat(ageTF.getText()),
                                    sexeTF.getText(),
                                    emailTF.getText(),
                                    mdpTF.getText(),
                                    telTF.getText(),
                                    adresseTF.getText(),
                                    selectedAbonnement,
                                    messageTF.getText(),
                                    selectedImage

                            ), imageEdited
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Abonne modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de abonne. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        }
    }

    private void showBackAndRefresh() {
        ((DisplayAll) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {


        if (nomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nom", new Command("Ok"));
            return false;
        }


        if (prenomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le prenom", new Command("Ok"));
            return false;
        }


        if (ageTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le age", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(ageTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", ageTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }


        if (sexeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le sexe", new Command("Ok"));
            return false;
        }


        if (emailTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le email", new Command("Ok"));
            return false;
        }


        if (mdpTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le mdp", new Command("Ok"));
            return false;
        }


        if (telTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le tel", new Command("Ok"));
            return false;
        }


        if (adresseTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le adresse", new Command("Ok"));
            return false;
        }


        if (messageTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le message", new Command("Ok"));
            return false;
        }


        if (selectedAbonnement == null) {
            Dialog.show("Avertissement", "Veuillez choisir un abonnement", new Command("Ok"));
            return false;
        }


        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }


        return true;
    }
}