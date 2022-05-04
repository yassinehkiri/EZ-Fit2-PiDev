package com.easyFit.gui.front.livraison;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.Livraison;
import com.easyFit.services.LivraisonService;

public class Manage extends Form {

    Label adresseLivraisonLabel;
    TextField adresseLivraisonTF;
    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super("Nouvelle livraison", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        adresseLivraisonLabel = new Label("Adresse : ");
        adresseLivraisonLabel.setUIID("labelDefault");
        adresseLivraisonTF = new TextField();
        adresseLivraisonTF.setHint("Tapez votre adresse de livraison");

        manageButton = new Button("Commander");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                adresseLivraisonLabel, adresseLivraisonTF,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
        manageButton.addActionListener(action -> {
            if (controleDeSaisie()) {
                int responseCode = LivraisonService.getInstance().add(
                        new Livraison(
                                0,
                                "vide",
                                "vide",
                                "vide",
                                adresseLivraisonTF.getText(),
                                null,
                                null
                        )
                );
                if (responseCode == 200) {
                    Dialog.show("Succés", "Livraison ajouté avec succes", new Command("Ok"));
                } else {
                    Dialog.show("Erreur", "Erreur d'ajout de livraison. Code d'erreur : " + responseCode, new Command("Ok"));
                }

                try {
                    ((DisplayAll) DisplayAll.livraisonForm).refresh();
                } catch (NullPointerException ignored) {

                }
                previous.showBack();
            }
        });
    }

    private boolean controleDeSaisie() {
        if (adresseLivraisonTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir l'adresse", new Command("Ok"));
            return false;
        }
        return true;
    }
}