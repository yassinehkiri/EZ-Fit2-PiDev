package com.easyFit.gui.back.equipement;

import com.codename1.components.InteractionDialog;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.Equipement;
import com.easyFit.services.EquipementService;

import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Equipement currentEquipement = null;
    Button addBtn;
    Label nomELabel, typeELabel, marqueLabel, gammeLabel, quantiteLabel, dateCommandeLabel, prixLabel, fournisseurLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Equipements", new BoxLayout(BoxLayout.Y_AXIS));

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

        this.add(addBtn);

        ArrayList<Equipement> listEquipements = EquipementService.getInstance().getAll();
        if (listEquipements.size() > 0) {
            for (Equipement listEquipement : listEquipements) {
                this.add(makeEquipementModel(listEquipement));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentEquipement = null;
            new Manage(this).show();
        });
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

        editBtn = new Button("Modifier");
        editBtn.addActionListener(action -> {
            currentEquipement = equipement;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce equipement ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = EquipementService.getInstance().delete(equipement.getId());

                if (responseCode == 200) {
                    currentEquipement = null;
                    dlg.dispose();
                    equipementModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du equipement. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        equipementModel.addAll(
                nomELabel, typeELabel, marqueLabel, gammeLabel, quantiteLabel, dateCommandeLabel, prixLabel,
                fournisseurLabel,
                btnsContainer
        );

        return equipementModel;
    }
}