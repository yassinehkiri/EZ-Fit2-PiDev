package com.easyFit.gui.back.livraison;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.Livraison;
import com.easyFit.services.LivraisonService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Manage extends Form {

    Livraison currentLivraison;

    Label nomLivreurLabel, prenomLivreurLabel, telLivreurLabel, adresseLivraisonLabel, dateDebutLabel;
    TextField nomLivreurTF, prenomLivreurTF, telLivreurTF, adresseLivraisonTF;
    Calendar calendar;
    Button manageButton;

    boolean selectedStart = false;
    Date selectedStartDate = null;
    Date selectedEndDate = null;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentLivraison == null ? "Nouvelle livraison" : "Modifier livraison", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentLivraison = DisplayAll.currentLivraison;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        nomLivreurLabel = new Label("Nom : ");
        nomLivreurLabel.setUIID("labelDefault");
        nomLivreurTF = new TextField();
        nomLivreurTF.setHint("Tapez le nom du livreur");

        prenomLivreurLabel = new Label("Prenom : ");
        prenomLivreurLabel.setUIID("labelDefault");
        prenomLivreurTF = new TextField();
        prenomLivreurTF.setHint("Tapez le prenom du livreur");

        telLivreurLabel = new Label("Tel : ");
        telLivreurLabel.setUIID("labelDefault");
        telLivreurTF = new TextField();
        telLivreurTF.setHint("Exp: 98100100");

        adresseLivraisonLabel = new Label("Adresse : ");
        adresseLivraisonLabel.setUIID("labelDefault");
        adresseLivraisonTF = new TextField();
        adresseLivraisonTF.setHint("Tapez l'addresse de livraison");

        dateDebutLabel = new Label("Date de livraison et date d'arrivé : ");
        dateDebutLabel.setUIID("labelDefault");

        calendar = new Calendar();

        if (currentLivraison == null) {
            manageButton = new Button("Ajouter");
        } else {
            selectedStartDate = currentLivraison.getDateDebut();
            selectedEndDate = currentLivraison.getDateFin();

            nomLivreurTF.setText(currentLivraison.getNomLivreur());
            telLivreurTF.setText(String.valueOf(currentLivraison.getTelLivreur()));

            if (selectedStartDate != null && selectedEndDate != null) {
                calendar.highlightDate(selectedStartDate, "dateStart");
                calendar.highlightDate(selectedEndDate, "dateEnd");
            }

            manageButton = new Button("Modifier");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                nomLivreurLabel, nomLivreurTF,
                prenomLivreurLabel, prenomLivreurTF,
                telLivreurLabel, telLivreurTF,
                adresseLivraisonLabel, adresseLivraisonTF,
                dateDebutLabel, calendar,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
        calendar.addActionListener(l -> {
            if (!selectedStart) {
                if (selectedStartDate != null) {
                    calendar.unHighlightDate(selectedStartDate);
                    calendar.unHighlightDate(selectedEndDate);
                }

                selectedStartDate = calendar.getDate();
                selectedEndDate = null;

                calendar.highlightDate(calendar.getDate(), "dateStart");
            } else {
                selectedEndDate = calendar.getDate();

                calendar.highlightDate(calendar.getDate(), "dateEnd");
            }

            selectedStart = !selectedStart;
        });

        if (currentLivraison == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = LivraisonService.getInstance().add(
                            new Livraison(
                                    1,
                                    nomLivreurTF.getText(),
                                    prenomLivreurTF.getText(),
                                    telLivreurTF.getText(),
                                    adresseLivraisonTF.getText(),
                                    selectedStartDate,
                                    selectedEndDate
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Livraison ajouté avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de livraison. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    ((DisplayAll) previous).refresh();
                    previous.showBack();
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = LivraisonService.getInstance().edit(
                            new Livraison(
                                    currentLivraison.getId(),
                                    1,
                                    nomLivreurTF.getText(),
                                    prenomLivreurTF.getText(),
                                    telLivreurTF.getText(),
                                    adresseLivraisonTF.getText(),
                                    selectedStartDate,
                                    selectedEndDate
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Livraison modifié avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de livraison. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    ((DisplayAll) previous).refresh();
                    previous.showBack();
                }
            });
        }
    }

    private boolean controleDeSaisie() {

        if (nomLivreurTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nom", new Command("Ok"));
            return false;
        }

        if (prenomLivreurTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le prenom", new Command("Ok"));
            return false;
        }

        if (telLivreurTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le tel", new Command("Ok"));
            return false;
        }
        if (telLivreurTF.getText().length() != 8) {
            Dialog.show("Avertissement", "Tel doit avoit 8 chiffres", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(telLivreurTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", telLivreurTF.getText() + " n'est pas un numero valide", new Command("Ok"));
            return false;
        }

        if (adresseLivraisonTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir l'adresse", new Command("Ok"));
            return false;
        }

        if (selectedStartDate == null) {
            Dialog.show("Avertissement", "Veuillez saisir la date d'expo", new Command("Ok"));
            return false;
        }
        if (selectedEndDate == null) {
            Dialog.show("Avertissement", "Veuillez saisir la date de fin", new Command("Ok"));
            return false;
        }
        if (dateIsAfter(selectedEndDate, selectedStartDate)) {
            Dialog.show("Avertissement", "Date de d'expo doit etre superieure a la date de fin", new Command("Ok"));
            return false;
        }

        return true;
    }

    boolean dateIsAfter(Date d1, Date d2) {

        int day1 = (int) Float.parseFloat(new SimpleDateFormat("dd").format(d1));
        int month1 = (int) Float.parseFloat(new SimpleDateFormat("MM").format(d1));
        int year1 = (int) Float.parseFloat(new SimpleDateFormat("yyyy").format(d1));

        int day2 = (int) Float.parseFloat(new SimpleDateFormat("dd").format(d2));
        int month2 = (int) Float.parseFloat(new SimpleDateFormat("MM").format(d2));
        int year2 = (int) Float.parseFloat(new SimpleDateFormat("yyyy").format(d2));

        if (year1 <= year2) {
            if (month1 <= month2) {
                return day1 <= day2;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}