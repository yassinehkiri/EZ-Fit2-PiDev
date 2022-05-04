package com.easyFit.gui.front.equipement;

import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.Equipement;
import com.easyFit.gui.front.AccueilFront;
import com.easyFit.gui.front.livraison.Manage;
import com.easyFit.services.EquipementService;

import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Equipement currentEquipement = null;
    Label nomELabel, typeELabel, marqueLabel, gammeLabel, quantiteLabel, dateCommandeLabel, prixLabel, fournisseurLabel;
    Button buyBtn;
    Container btnsContainer;

    public DisplayAll() {
        super("Equipements", new BoxLayout(BoxLayout.Y_AXIS));

        addGUIs();

        super.getToolbar().hideToolbar();
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        this.refreshTheme();
    }

    private void addGUIs() {
        ArrayList<Equipement> listEquipements = EquipementService.getInstance().getAll();
        if (listEquipements.size() > 0) {
            for (Equipement listEquipement : listEquipements) {
                this.add(makeEquipementModel(listEquipement));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private Component makeEquipementModel(Equipement equipement) {
        Container equipementModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        equipementModel.setUIID("containerRounded");

        nomELabel = new Label("Nom : " + equipement.getNomE());
        nomELabel.setUIID("labelDefault");

        typeELabel = new Label("Type : " + equipement.getTypeE());
        typeELabel.setUIID("labelDefault");

        marqueLabel = new Label("Marque : " + equipement.getMarque());
        marqueLabel.setUIID("labelDefault");

        gammeLabel = new Label("Gamme : " + equipement.getGamme());
        gammeLabel.setUIID("labelDefault");

        quantiteLabel = new Label("Quantite : " + equipement.getQuantite());
        quantiteLabel.setUIID("labelDefault");

        dateCommandeLabel = new Label("Date : " + new SimpleDateFormat("dd-MM-yyyy").format(equipement.getDateCommande()));
        dateCommandeLabel.setUIID("labelDefault");

        prixLabel = new Label("Prix : " + equipement.getPrix());
        prixLabel.setUIID("labelDefault");

        fournisseurLabel = new Label("Fournisseur : " + equipement.getFournisseur().getNomF());
        fournisseurLabel.setUIID("labelDefault");

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        buyBtn = new Button("Acheter");
        buyBtn.addActionListener(action -> {
            currentEquipement = equipement;
            new Manage(AccueilFront.accueilFrontForm).show();
        });

        btnsContainer.add(BorderLayout.CENTER, buyBtn);

        equipementModel.addAll(
                nomELabel, typeELabel, marqueLabel, gammeLabel, quantiteLabel, dateCommandeLabel, prixLabel,
                fournisseurLabel,
                btnsContainer
        );

        return equipementModel;
    }
}