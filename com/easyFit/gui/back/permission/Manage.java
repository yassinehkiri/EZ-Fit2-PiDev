package com.easyFit.gui.back.permission;


import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.entities.Permission;
import com.easyFit.entities.Personnel;
import com.easyFit.services.PermissionService;
import com.easyFit.utils.Statics;

import java.io.IOException;

public class Manage extends Form {


    public static Personnel selectedPersonnel;
    Resources theme = UIManager.initFirstTheme("/theme");
    String selectedImage;
    boolean imageEdited = false;
    Permission currentPermission;
    Label typeLabel, reclamationLabel, dateDebutLabel, dateFinLabel, personnelLabel, imageLabel;
    TextField
            typeTF,
            reclamationTF,
            personnelTF,
            imageTF, elemTF;
    PickerComponent dateDebutTF;
    PickerComponent dateFinTF;

    Label selectedPersonnelLabel;
    Button selectPersonnelButton;


    ImageViewer imageIV;
    Button selectImageButton;

    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentPermission == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;


        selectedPersonnel = null;

        currentPermission = DisplayAll.currentPermission;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }


    public void refreshPersonnel() {
        selectedPersonnelLabel.setText(selectedPersonnel.getNom());
        selectPersonnelButton.setText("Choisir  personnel");
        this.refreshTheme();
    }


    private void addGUIs() {


        typeLabel = new Label("Type : ");
        typeLabel.setUIID("labelDefault");
        typeTF = new TextField();
        typeTF.setHint("Tapez le type");

        reclamationLabel = new Label("Reclamation : ");
        reclamationLabel.setUIID("labelDefault");
        reclamationTF = new TextField();
        reclamationTF.setHint("Tapez le reclamation");
        dateDebutTF = PickerComponent.createDate(null).label("DateDebut");
        dateFinTF = PickerComponent.createDate(null).label("DateFin");

        personnelLabel = new Label("Personnel : ");
        personnelLabel.setUIID("labelDefault");
        personnelTF = new TextField();
        personnelTF.setHint("Tapez le personnel");


        personnelLabel = new Label("personnel : ");
        personnelLabel.setUIID("labelDefault");
        selectedPersonnelLabel = new Label("null");
        selectPersonnelButton = new Button("Choisir personnel");
        selectPersonnelButton.addActionListener(l -> new ChoosePersonnel(this).show());


        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        if (currentPermission == null) {

            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(500, 500));


            manageButton = new Button("Ajouter");
        } else {


            typeTF.setText(currentPermission.getType());
            reclamationTF.setText(currentPermission.getReclamation());
            dateDebutTF.getPicker().setDate(currentPermission.getDateDebut());
            dateFinTF.getPicker().setDate(currentPermission.getDateFin());


            selectedPersonnel = currentPermission.getPersonnel();

            personnelLabel = new Label("personnel : ");
            personnelLabel.setUIID("labelDefault");
            selectedPersonnelLabel = new Label("null");
            selectedPersonnelLabel.setText(selectedPersonnel.getNom());
            selectPersonnelButton.setText("Choisir  personnel");

            if (currentPermission.getImage() != null) {
                selectedImage = currentPermission.getImage();
                String url = Statics.PERMISSION_IMAGE_URL + currentPermission.getImage();
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

                typeLabel, typeTF, reclamationLabel, reclamationTF, dateDebutTF, dateFinTF,
                personnelLabel,
                selectedPersonnelLabel, selectPersonnelButton,
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

        if (currentPermission == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = PermissionService.getInstance().add(
                            new Permission(


                                    typeTF.getText(),
                                    reclamationTF.getText(),
                                    dateDebutTF.getPicker().getDate(),
                                    dateFinTF.getPicker().getDate(),
                                    selectedPersonnel,
                                    selectedImage
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Permission ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de permission. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = PermissionService.getInstance().edit(
                            new Permission(
                                    currentPermission.getId(),


                                    typeTF.getText(),
                                    reclamationTF.getText(),
                                    dateDebutTF.getPicker().getDate(),
                                    dateFinTF.getPicker().getDate(),
                                    selectedPersonnel,
                                    selectedImage

                            ), imageEdited
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Permission modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de permission. Code d'erreur : " + responseCode, new Command("Ok"));
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


        if (typeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le type", new Command("Ok"));
            return false;
        }


        if (reclamationTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le reclamation", new Command("Ok"));
            return false;
        }


        if (dateDebutTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la dateDebut", new Command("Ok"));
            return false;
        }


        if (dateFinTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la dateFin", new Command("Ok"));
            return false;
        }


        if (selectedPersonnel == null) {
            Dialog.show("Avertissement", "Veuillez choisir un personnel", new Command("Ok"));
            return false;
        }


        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }


        return true;
    }
}