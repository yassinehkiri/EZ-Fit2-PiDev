package com.easyFit.gui.back.salle;


import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;

import com.easyFit.entities.Salle;
import com.easyFit.services.SalleService;
import com.easyFit.utils.Statics;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class Manage extends Form {

    
    Resources theme = UIManager.initFirstTheme("/theme");
    String selectedImage;
    boolean imageEdited = false;
    

    Salle currentSalle;

    
        
    Label nomLabel , adresseLabel , codePostalLabel , villeLabel , nombreLabel , imageLabel , longitudeLabel , lattitudeLabel ;
    TextField 
    nomTF , 
    adresseTF , 
    codePostalTF , 
    villeTF , 
    nombreTF , 
    imageTF , 
    longitudeTF , 
    lattitudeTF  ,elemTF;
    
    
    
    ImageViewer imageIV;
    Button selectImageButton;
    
    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentSalle == null ?  "Ajouter" :  "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        
        currentSalle = DisplayAll.currentSalle;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    

    private void addGUIs() {
        
        
        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");
        
        adresseLabel = new Label("Adresse : ");
        adresseLabel.setUIID("labelDefault");
        adresseTF = new TextField();
        adresseTF.setHint("Tapez le adresse");
        
        codePostalLabel = new Label("CodePostal : ");
        codePostalLabel.setUIID("labelDefault");
        codePostalTF = new TextField();
        codePostalTF.setHint("Tapez le codePostal");
        
        villeLabel = new Label("Ville : ");
        villeLabel.setUIID("labelDefault");
        villeTF = new TextField();
        villeTF.setHint("Tapez le ville");
        
        nombreLabel = new Label("Nombre : ");
        nombreLabel.setUIID("labelDefault");
        nombreTF = new TextField();
        nombreTF.setHint("Tapez le nombre");
        
        
        
        longitudeLabel = new Label("Longitude : ");
        longitudeLabel.setUIID("labelDefault");
        longitudeTF = new TextField();
        longitudeTF.setHint("Tapez le longitude");
        
        lattitudeLabel = new Label("Lattitude : ");
        lattitudeLabel.setUIID("labelDefault");
        lattitudeTF = new TextField();
        lattitudeTF.setHint("Tapez le lattitude");

        

        
        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        if (currentSalle == null) {
            
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(700, 500));
            
            
            manageButton = new Button("Ajouter");
        } else {
    
            
            nomTF.setText(currentSalle.getNom());
            adresseTF.setText(currentSalle.getAdresse());
            codePostalTF.setText(String.valueOf(currentSalle.getCodePostal()));
            villeTF.setText(currentSalle.getVille());
            nombreTF.setText(String.valueOf(currentSalle.getNombre()));
            
            longitudeTF.setText(currentSalle.getLongitude());
            lattitudeTF.setText(currentSalle.getLattitude());
            
            if (currentSalle.getImage() != null) {
                selectedImage = currentSalle.getImage();
                String url = Statics.SALLE_IMAGE_URL + currentSalle.getImage();
                Image image = URLImage.createToStorage(
                        EncodedImage.createFromImage(theme.getImage("default.jpg").fill(700, 500), false),
                        url,
                        url,
                        URLImage.RESIZE_SCALE
                );
                imageIV = new ImageViewer(image);
            } else {
                imageIV = new ImageViewer(theme.getImage("default.jpg").fill(700, 500));
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
            
            nomLabel, nomTF,adresseLabel, adresseTF,codePostalLabel, codePostalTF,villeLabel, villeTF,nombreLabel, nombreTF,longitudeLabel, longitudeTF,lattitudeLabel, lattitudeTF,
            
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
        
        if (currentSalle == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = SalleService.getInstance().add(
                            new Salle(
                                    
                                    
                                    nomTF.getText(),
                                    adresseTF.getText(),
                                    (int) Float.parseFloat(codePostalTF.getText()),
                                    villeTF.getText(),
                                    (int) Float.parseFloat(nombreTF.getText()),
                                    selectedImage,
                                    longitudeTF.getText(),
                                    lattitudeTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Salle ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de salle. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = SalleService.getInstance().edit(
                            new Salle(
                                    currentSalle.getId(),
                                    
                                    
                                    nomTF.getText(),
                                    adresseTF.getText(),
                                    (int) Float.parseFloat(codePostalTF.getText()),
                                    villeTF.getText(),
                                    (int) Float.parseFloat(nombreTF.getText()),
                                    selectedImage,
                                    longitudeTF.getText(),
                                    lattitudeTF.getText()

                            ), imageEdited
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Salle modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de salle. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        }
    }

    private void showBackAndRefresh(){
        ((DisplayAll) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        
        
        if (nomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nom", new Command("Ok"));
            return false;
        }
        
        
        
        
        if (adresseTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le adresse", new Command("Ok"));
            return false;
        }
        
        
        
        
        if (codePostalTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le codePostal", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(codePostalTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", codePostalTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }
        
        
        
        
        if (villeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le ville", new Command("Ok"));
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
        
        
        
        
        
        
        
        if (longitudeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le longitude", new Command("Ok"));
            return false;
        }
        
        
        
        
        if (lattitudeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le lattitude", new Command("Ok"));
            return false;
        }
        
        
        

        

        
        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }
        
             
        return true;
    }
}