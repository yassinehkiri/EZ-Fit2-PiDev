package com.easyFit.gui.back.equipement;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.Equipement;
import com.easyFit.entities.Fournisseur;
import com.easyFit.services.EquipementService;

import java.util.Date;

public class Manage extends Form {

    public static Fournisseur selectedFournisseur;
    Equipement currentEquipement;

    Label nomELabel, typeELabel, marqueLabel, gammeLabel, quantiteLabel, prixLabel, fournisseurLabel, selectedFournisseurLabel;
    TextField nomETF, typeETF, marqueTF, gammeTF, quantiteTF, prixTF;
    PickerComponent dateTF;
    Button selectFournisseurButton, manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentEquipement == null ? "Ajouter equipement" :  "Modifier equipement", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        selectedFournisseur = null;
        currentEquipement = DisplayAll.currentEquipement;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refreshFournisseur() {
        selectedFournisseurLabel.setText(selectedFournisseur.getNomF());
        selectFournisseurButton.setText("Choisir  fournisseur");
        this.refreshTheme();
    }

    private void addGUIs() {

        nomELabel = new Label("Nom : ");
        nomELabel.setUIID("labelDefault");
        nomETF = new TextField();
        nomETF.setHint("Tapez le nom de l'equipement");

        typeELabel = new Label("Type : ");
        typeELabel.setUIID("labelDefault");
        typeETF = new TextField();
        typeETF.setHint("Tapez le type de l'equipement");

        marqueLabel = new Label("Marque : ");
        marqueLabel.setUIID("labelDefault");
        marqueTF = new TextField();
        marqueTF.setHint("Tapez la marque de l'equipement");

        gammeLabel = new Label("Gamme : ");
        gammeLabel.setUIID("labelDefault");
        gammeTF = new TextField();
        gammeTF.setHint("Tapez la gamme de l'equipement");

        quantiteLabel = new Label("Quantite : ");
        quantiteLabel.setUIID("labelDefault");
        quantiteTF = new TextField();
        quantiteTF.setHint("Tapez la quantite de l'equipement");

        prixLabel = new Label("Prix : ");
        prixLabel.setUIID("labelDefault");
        prixTF = new TextField();
        prixTF.setHint("Tapez le prix de l'equipement");

        dateTF = PickerComponent.createDate(null).label("Date de fabrication");

        if (currentEquipement == null) {
            manageButton = new Button("Ajouter");
        } else {
            nomETF.setText(currentEquipement.getNomE());
            typeETF.setText(currentEquipement.getTypeE());
            marqueTF.setText(currentEquipement.getMarque());
            gammeTF.setText(currentEquipement.getGamme());
            quantiteTF.setText(String.valueOf(currentEquipement.getQuantite()));
            prixTF.setText(String.valueOf(currentEquipement.getPrix()));
            dateTF.getPicker().setDate(currentEquipement.getDateCommande());
            selectedFournisseur = currentEquipement.getFournisseur();

            manageButton = new Button("Modifier");
        }

        fournisseurLabel = new Label("Fournisseur : ");
        fournisseurLabel.setUIID("labelDefault");
        if (selectedFournisseur != null) {
            selectedFournisseurLabel = new Label(selectedFournisseur.getNomF());
            selectFournisseurButton = new Button("Modifier fournisseur");
        } else {
            selectedFournisseurLabel = new Label(/*  */"Aucun fournisseur selectionné");
            selectFournisseurButton = new Button(/*  */"Choisir un fournisseur");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                nomELabel, nomETF,
                typeELabel, typeETF,
                marqueLabel, marqueTF,
                gammeLabel, gammeTF,
                quantiteLabel, quantiteTF,
                prixLabel, prixTF,
                dateTF,
                fournisseurLabel, selectedFournisseurLabel, selectFournisseurButton,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
        selectFournisseurButton.addActionListener(l -> new ChooseFournisseur(this).show());

        if (currentEquipement == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = EquipementService.getInstance().add(
                            new Equipement(
                                    nomETF.getText(),
                                    typeETF.getText(),
                                    marqueTF.getText(),
                                    gammeTF.getText(),
                                    (int) Float.parseFloat(quantiteTF.getText()),
                                    dateTF.getPicker().getDate(),
                                    Float.parseFloat(prixTF.getText()),
                                    selectedFournisseur
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Equipement ajouté avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de equipement. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = EquipementService.getInstance().edit(
                            new Equipement(
                                    currentEquipement.getId(),
                                    nomETF.getText(),
                                    typeETF.getText(),
                                    marqueTF.getText(),
                                    gammeTF.getText(),
                                    (int) Float.parseFloat(quantiteTF.getText()),
                                    dateTF.getPicker().getDate(),
                                    Float.parseFloat(prixTF.getText()),
                                    selectedFournisseur
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Equipement modifié avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de equipement. Code d'erreur : " + responseCode, new Command("Ok"));
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
        if (nomETF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nom", new Command("Ok"));
            return false;
        }

        if (typeETF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le type", new Command("Ok"));
            return false;
        }

        if (marqueTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le marque", new Command("Ok"));
            return false;
        }

        if (gammeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le gamme", new Command("Ok"));
            return false;
        }

        if (quantiteTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le quantite", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(quantiteTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", quantiteTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
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

        if (dateTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la date de fabrication", new Command("Ok"));
            return false;
        }

        if (selectedFournisseur == null) {
            Dialog.show("Avertissement", "Veuillez choisir un fournisseur", new Command("Ok"));
            return false;
        }

        return true;
    }
}