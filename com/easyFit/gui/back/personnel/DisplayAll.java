package com.easyFit.gui.back.personnel;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.Personnel;
import com.easyFit.services.PersonnelService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {


    public static Personnel currentPersonnel = null;
    Button addBtn;
    Label nomLabel, prenomLabel, dateEmbaucheLabel, telLabel, emailLabel, passwordLabel, salaireLabel, posteLabel, hTravailLabel, hAbsenceLabel, salleLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Personnels", new BoxLayout(BoxLayout.Y_AXIS));

        addGUIs();
        addActions();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        addActions();
        this.refreshTheme();
    }

    private void addGUIs() {
        addBtn = new Button("Ajouter");
        addBtn.setUIID("buttonWhiteCenter");

        this.add(addBtn);

        ArrayList<Personnel> listPersonnels = PersonnelService.getInstance().getAll();
        if (listPersonnels.size() > 0) {
            for (Personnel listPersonnel : listPersonnels) {
                this.add(makePersonnelModel(listPersonnel));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentPersonnel = null;
            new Manage(this).show();
        });
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


        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonMain");
        editBtn.addActionListener(action -> {
            currentPersonnel = personnel;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce personnel ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = PersonnelService.getInstance().delete(personnel.getId());

                if (responseCode == 200) {
                    currentPersonnel = null;
                    dlg.dispose();
                    personnelModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du personnel. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.CENTER, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

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