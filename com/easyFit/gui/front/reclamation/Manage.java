package com.easyFit.gui.front.reclamation;


import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.entities.Reclamation;
import com.easyFit.services.ReclamationService;
import com.easyFit.utils.Statics;

import java.io.IOException;

public class Manage extends Form {


    Resources theme = UIManager.initFirstTheme("/theme");
    String selectedImage;
    boolean imageEdited = false;


    Reclamation currentReclamation;


    Label redacteurLabel, dateLabel, contenuLabel, imageLabel;
    TextField
            redacteurTF,
            contenuTF,
            imageTF, elemTF;
    PickerComponent dateTF;


    ImageViewer imageIV;
    Button selectImageButton;

    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentReclamation == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;
        currentReclamation = DisplayAll.currentReclamation;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }


    private void addGUIs() {


        redacteurLabel = new Label("Redacteur : ");
        redacteurLabel.setUIID("labelDefault");
        redacteurTF = new TextField();
        redacteurTF.setHint("Tapez le redacteur");
        dateTF = PickerComponent.createDate(null).label("Date");

        contenuLabel = new Label("Contenu : ");
        contenuLabel.setUIID("labelDefault");
        contenuTF = new TextField();
        contenuTF.setHint("Tapez le contenu");


        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        if (currentReclamation == null) {

            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(500, 500));


            manageButton = new Button("Ajouter");
        } else {


            redacteurTF.setText(currentReclamation.getRedacteur());
            dateTF.getPicker().setDate(currentReclamation.getDate());
            contenuTF.setText(currentReclamation.getContenu());


            if (currentReclamation.getImage() != null) {
                String url = Statics.RECLAMATION_IMAGE_URL + currentReclamation.getImage();
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
                redacteurLabel, redacteurTF, dateTF, contenuLabel, contenuTF,
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

        if (currentReclamation == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = ReclamationService.getInstance().add(
                            new Reclamation(


                                    redacteurTF.getText(),
                                    dateTF.getPicker().getDate(),
                                    contenuTF.getText(),
                                    selectedImage
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Reclamation ajouté avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de reclamation. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = ReclamationService.getInstance().edit(
                            new Reclamation(
                                    currentReclamation.getId(),


                                    redacteurTF.getText(),
                                    dateTF.getPicker().getDate(),
                                    contenuTF.getText(),
                                    selectedImage

                            ), imageEdited
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Reclamation modifié avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de reclamation. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        }
    }

    private void showBackAndRefresh() {
        ((DisplayAll) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {


        if (redacteurTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le redacteur", new Command("Ok"));
            return false;
        }


        if (dateTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la date", new Command("Ok"));
            return false;
        }


        if (contenuTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le contenu", new Command("Ok"));
            return false;
        }


        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }


        return true;
    }
}