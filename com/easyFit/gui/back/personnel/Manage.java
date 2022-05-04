package com.easyFit.gui.back.personnel;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.Personnel;
import com.easyFit.entities.Salle;
import com.easyFit.services.PersonnelService;

public class Manage extends Form {


    public static Salle selectedSalle;
    Personnel currentPersonnel;
    Label nomLabel, prenomLabel, dateEmbaucheLabel, telLabel, emailLabel, passwordLabel, salaireLabel, posteLabel, hTravailLabel, hAbsenceLabel, salleLabel;
    TextField
            nomTF,
            prenomTF,
            telTF,
            emailTF,
            passwordTF,
            salaireTF,
            posteTF,
            hTravailTF,
            hAbsenceTF;
    PickerComponent dateEmbaucheTF;

    Label selectedSalleLabel;
    Button selectSalleButton;


    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentPersonnel == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;


        selectedSalle = null;

        currentPersonnel = DisplayAll.currentPersonnel;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }


    public void refreshSalle() {
        selectedSalleLabel.setText(selectedSalle.getNom());
        selectSalleButton.setText("Choisir  salle");
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
        dateEmbaucheTF = PickerComponent.createDate(null).label("DateEmbauche");

        telLabel = new Label("Tel : ");
        telLabel.setUIID("labelDefault");
        telTF = new TextField();
        telTF.setHint("Tapez le tel");

        emailLabel = new Label("Email : ");
        emailLabel.setUIID("labelDefault");
        emailTF = new TextField();
        emailTF.setHint("Tapez le email");

        passwordLabel = new Label("Password : ");
        passwordLabel.setUIID("labelDefault");
        passwordTF = new TextField();
        passwordTF.setHint("Tapez le password");

        salaireLabel = new Label("Salaire : ");
        salaireLabel.setUIID("labelDefault");
        salaireTF = new TextField();
        salaireTF.setHint("Tapez le salaire");

        posteLabel = new Label("Poste : ");
        posteLabel.setUIID("labelDefault");
        posteTF = new TextField();
        posteTF.setHint("Tapez le poste");

        hTravailLabel = new Label("HTravail : ");
        hTravailLabel.setUIID("labelDefault");
        hTravailTF = new TextField();
        hTravailTF.setHint("Tapez le hTravail");

        hAbsenceLabel = new Label("Heure d'absence : ");
        hAbsenceLabel.setUIID("labelDefault");
        hAbsenceTF = new TextField();
        hAbsenceTF.setHint("Tapez les heures d'absence");

        salleLabel = new Label("salle : ");
        salleLabel.setUIID("labelDefault");
        selectedSalleLabel = new Label("null");
        selectSalleButton = new Button("Choisir salle");
        selectSalleButton.addActionListener(l -> new ChooseSalle(this).show());


        if (currentPersonnel == null) {


            manageButton = new Button("Ajouter");
        } else {


            nomTF.setText(currentPersonnel.getNom());
            prenomTF.setText(currentPersonnel.getPrenom());
            dateEmbaucheTF.getPicker().setDate(currentPersonnel.getDateEmbauche());
            telTF.setText(String.valueOf(currentPersonnel.getTel()));
            emailTF.setText(currentPersonnel.getEmail());
            passwordTF.setText(currentPersonnel.getPassword());
            salaireTF.setText(String.valueOf(currentPersonnel.getSalaire()));
            posteTF.setText(String.valueOf(currentPersonnel.getPoste()));
            hTravailTF.setText(String.valueOf(currentPersonnel.getHTravail()));
            hAbsenceTF.setText(String.valueOf(currentPersonnel.getHAbsence()));

            selectedSalle = currentPersonnel.getSalle();

            salleLabel = new Label("salle : ");
            salleLabel.setUIID("labelDefault");
            selectedSalleLabel = new Label("null");
            selectedSalleLabel.setText(selectedSalle.getNom());
            selectSalleButton = new Button();
            selectSalleButton.setText("Choisir  salle");


            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonMain");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(

                nomLabel, nomTF,
                prenomLabel, prenomTF,
                dateEmbaucheTF,
                telLabel, telTF,
                emailLabel, emailTF,
                passwordLabel, passwordTF,
                salaireLabel, salaireTF,
                posteLabel, posteTF,
                hTravailLabel, hTravailTF,
                hAbsenceLabel, hAbsenceTF,
                salleLabel,
                selectedSalleLabel, selectSalleButton,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentPersonnel == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = PersonnelService.getInstance().add(
                            new Personnel(


                                    nomTF.getText(),
                                    prenomTF.getText(),
                                    dateEmbaucheTF.getPicker().getDate(),
                                    (int) Float.parseFloat(telTF.getText()),
                                    emailTF.getText(),
                                    passwordTF.getText(),
                                    Float.parseFloat(salaireTF.getText()),
                                    (int) Float.parseFloat(posteTF.getText()),
                                    (int) Float.parseFloat(hTravailTF.getText()),
                                    (int) Float.parseFloat(hAbsenceTF.getText()),
                                    selectedSalle
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Personnel ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de personnel. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = PersonnelService.getInstance().edit(
                            new Personnel(
                                    currentPersonnel.getId(),


                                    nomTF.getText(),
                                    prenomTF.getText(),
                                    dateEmbaucheTF.getPicker().getDate(),
                                    (int) Float.parseFloat(telTF.getText()),
                                    emailTF.getText(),
                                    passwordTF.getText(),
                                    Float.parseFloat(salaireTF.getText()),
                                    (int) Float.parseFloat(posteTF.getText()),
                                    (int) Float.parseFloat(hTravailTF.getText()),
                                    (int) Float.parseFloat(hAbsenceTF.getText()),
                                    selectedSalle

                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Personnel modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de personnel. Code d'erreur : " + responseCode, new Command("Ok"));
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


        if (dateEmbaucheTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la dateEmbauche", new Command("Ok"));
            return false;
        }


        if (telTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le tel", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(telTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", telTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }


        if (emailTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le email", new Command("Ok"));
            return false;
        }


        if (passwordTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le password", new Command("Ok"));
            return false;
        }


        if (salaireTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le salaire", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(salaireTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", salaireTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }


        if (posteTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le poste", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(posteTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", posteTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }


        if (hTravailTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le hTravail", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(hTravailTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", hTravailTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }


        if (hAbsenceTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le hAbsence", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(hAbsenceTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", hAbsenceTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }


        if (selectedSalle == null) {
            Dialog.show("Avertissement", "Veuillez choisir un salle", new Command("Ok"));
            return false;
        }


        return true;
    }
}