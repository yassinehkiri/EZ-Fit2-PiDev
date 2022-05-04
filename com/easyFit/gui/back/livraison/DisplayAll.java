package com.easyFit.gui.back.livraison;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.Livraison;
import com.easyFit.services.LivraisonService;

import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Livraison currentLivraison = null;
    Button addBtn;
    Label numLLabel, nomLivreurLabel, prenomLivreurLabel, telLivreurLabel, adresseLivraisonLabel, dateLabel;
    Calendar calendar;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Livraisons", new BoxLayout(BoxLayout.Y_AXIS));

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
        addBtn = new Button("Nouvelle livraison");

        this.add(addBtn);
        ArrayList<Livraison> listLivraisons = LivraisonService.getInstance().getAll();
        if (listLivraisons.size() > 0) {
            for (Livraison livraisons : listLivraisons) {
                this.add(makeLivraisonModel(livraisons));
            }
        } else {
            this.add(new Label("Aucune livraison"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentLivraison = null;
            new Manage(this).show();
        });
    }

    private Component makeLivraisonModel(Livraison livraison) {
        Container livraisonModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        livraisonModel.setUIID("containerRounded");

        if (livraison.getNumL() == 0) {
            numLLabel = new Label("Etat de livraison : non traité");
        } else {
            numLLabel = new Label("Etat de livraison : traité");
        }
        numLLabel.setUIID("labelDefault");

        nomLivreurLabel = new Label("Nom du livreur : " + livraison.getNomLivreur());
        nomLivreurLabel.setUIID("labelDefault");

        prenomLivreurLabel = new Label("Prenom du livreur : " + livraison.getNomLivreur());
        prenomLivreurLabel.setUIID("labelDefault");

        telLivreurLabel = new Label("Tel du livreur : " + livraison.getTelLivreur());
        telLivreurLabel.setUIID("labelDefault");

        adresseLivraisonLabel = new Label("Adresse de livraison : " + livraison.getTelLivreur());
        adresseLivraisonLabel.setUIID("labelDefault");

        dateLabel = new Label("Date de livraison et date d'arrivé");
        adresseLivraisonLabel.setUIID("labelCenter");

        editBtn = new Button("Modifier");

        if (livraison.getDateDebut() != null && livraison.getDateFin() != null) {
            calendar = new Calendar();
            calendar.setFocusable(false);
            calendar.highlightDate(livraison.getDateDebut(), "dateStart");
            calendar.highlightDate(livraison.getDateFin(), "dateEnd");
        } else {
            editBtn.setText("Traiter");
        }

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn.addActionListener(action -> {
            currentLivraison = livraison;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.addActionListener(l -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce livraison ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = LivraisonService.getInstance().delete(livraison.getId());

                if (responseCode == 200) {
                    currentLivraison = null;
                    dlg.dispose();
                    livraisonModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression de livraison. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        livraisonModel.addAll(numLLabel, nomLivreurLabel, prenomLivreurLabel, telLivreurLabel, adresseLivraisonLabel, dateLabel);
        if (livraison.getDateDebut() != null && livraison.getDateDebut() != null) {
            livraisonModel.add(calendar);
        }
        livraisonModel.add(btnsContainer);
        return livraisonModel;
    }
}