package com.easyFit.gui.back.permission;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.Personnel;
import com.easyFit.services.PersonnelService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChoosePersonnel extends Form {

    Form previousForm;
    Label nomLabel, prenomLabel, dateEmbaucheLabel, telLabel, emailLabel, passwordLabel, salaireLabel, posteLabel, hTravailLabel, hAbsenceLabel, salleLabel;
    Button chooseBtn;
    Container btnsContainer;

    public ChoosePersonnel(Form previous) {
        super("Choisir un personnel", new BoxLayout(BoxLayout.Y_AXIS));

        previousForm = previous;
        addGUIs();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        this.refreshTheme();
    }

    private void addGUIs() {
        ArrayList<Personnel> listPersonnels = PersonnelService.getInstance().getAll();
        if (listPersonnels.size() > 0) {
            for (Personnel personnels : listPersonnels) {
                this.add(makePersonnelModel(personnels));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private Component makePersonnelModel(Personnel personnel) {
        Container personnelModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        personnelModel.setUIID("containerRounded");

        nomLabel = new Label("Nom : " + personnel.getNom());

        nomLabel.setUIID("labelDefault");


        prenomLabel = new Label("Prenom : " + personnel.getPrenom());

        prenomLabel.setUIID("labelDefault");


        dateEmbaucheLabel = new Label("DateEmbauche : " + new SimpleDateFormat("dd-MM-yyyy").format(personnel.getDateEmbauche()));

        dateEmbaucheLabel.setUIID("labelDefault");


        telLabel = new Label("Tel : " + personnel.getTel());

        telLabel.setUIID("labelDefault");


        emailLabel = new Label("Email : " + personnel.getEmail());

        emailLabel.setUIID("labelDefault");


        passwordLabel = new Label("Password : " + personnel.getPassword());

        passwordLabel.setUIID("labelDefault");


        salaireLabel = new Label("Salaire : " + personnel.getSalaire());

        salaireLabel.setUIID("labelDefault");


        posteLabel = new Label("Poste : " + personnel.getPoste());

        posteLabel.setUIID("labelDefault");


        hTravailLabel = new Label("HTravail : " + personnel.getHTravail());

        hTravailLabel.setUIID("labelDefault");


        hAbsenceLabel = new Label("HAbsence : " + personnel.getHAbsence());

        hAbsenceLabel.setUIID("labelDefault");


        salleLabel = new Label("Salle : " + personnel.getSalle());

        salleLabel.setUIID("labelDefault");


        salleLabel = new Label("Salle : " + personnel.getSalle().getNom());
        salleLabel.setUIID("labelDefault");
        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            Manage.selectedPersonnel = personnel;
            ((Manage) previousForm).refreshPersonnel();
            previousForm.showBack();
        });

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        personnelModel.addAll(
                nomLabel, prenomLabel,
                dateEmbaucheLabel, telLabel,
                emailLabel, passwordLabel,
                salaireLabel, posteLabel,
                hTravailLabel, hAbsenceLabel,
                salleLabel,
                btnsContainer
        );

        return personnelModel;
    }
}