package com.easyFit.gui.back.abonne;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.Abonnement;
import com.easyFit.services.AbonnementService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChooseAbonnement extends Form {

    Form previousForm;

    Label typeLabel, tarifLabel, dateDebutLabel, dateFinLabel;
    Button chooseBtn;
    Container btnsContainer;

    public ChooseAbonnement(Form previous) {
        super("Choisir un abonnement", new BoxLayout(BoxLayout.Y_AXIS));

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
        ArrayList<Abonnement> listAbonnements = AbonnementService.getInstance().getAll();
        if (listAbonnements.size() > 0) {
            for (Abonnement abonnements : listAbonnements) {
                this.add(makeAbonnementModel(abonnements));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private Component makeAbonnementModel(Abonnement abonnement) {
        Container abonnementModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        abonnementModel.setUIID("containerRounded");

        typeLabel = new Label("Type : " + abonnement.getType());
        typeLabel.setUIID("labelDefault");
        tarifLabel = new Label("Tarif : " + abonnement.getTarif());
        tarifLabel.setUIID("labelDefault");
        dateDebutLabel = new Label("DateDebut : " + new SimpleDateFormat("dd-MM-yyyy").format(abonnement.getDateDebut()));
        dateDebutLabel.setUIID("labelDefault");
        dateFinLabel = new Label("DateFin : " + new SimpleDateFormat("dd-MM-yyyy").format(abonnement.getDateFin()));
        dateFinLabel.setUIID("labelDefault");

        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            Manage.selectedAbonnement = abonnement;
            ((Manage) previousForm).refreshAbonnement();
            previousForm.showBack();
        });

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, chooseBtn);


        abonnementModel.addAll(
                typeLabel, tarifLabel, dateDebutLabel, dateFinLabel,
                btnsContainer
        );


        return abonnementModel;
    }
}