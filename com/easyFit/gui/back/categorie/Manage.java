package com.easyFit.gui.back.categorie;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.Categorie;
import com.easyFit.gui.back.produit.ChooseCategorie;
import com.easyFit.services.CategorieService;

public class Manage extends Form {

    Categorie currentCategorie;
    boolean isChoosing;

    Label nomCLabel, typeLabel;
    TextField nomCTF, typeTF;
    Button manageButton;

    Form previous;

    public Manage(Form previous, boolean isChoosing) {
        super(DisplayAll.currentCategorie == null ? "Nouvelle categorie" : "Modifier categorie", new BoxLayout(BoxLayout.Y_AXIS));

        this.previous = previous;

        this.isChoosing = isChoosing;
        currentCategorie = DisplayAll.currentCategorie;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        nomCLabel = new Label("Nom : ");
        nomCLabel.setUIID("labelDefault");
        nomCTF = new TextField();
        nomCTF.setHint("Tapez le nom");

        typeLabel = new Label("Type : ");
        typeLabel.setUIID("labelDefault");
        typeTF = new TextField();
        typeTF.setHint("Tapez le type");

        if (currentCategorie == null) {
            manageButton = new Button("Ajouter");
        } else {
            nomCTF.setText(currentCategorie.getNomC());
            typeTF.setText(currentCategorie.getType());

            manageButton = new Button("Modifier");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                nomCLabel, nomCTF,
                typeLabel, typeTF,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
        if (currentCategorie == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = CategorieService.getInstance().add(
                            new Categorie(
                                    nomCTF.getText(),
                                    typeTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Categorie ajouté avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de categorie. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    if (isChoosing) {
                        ((ChooseCategorie) previous).refresh();
                    } else {
                        ((DisplayAll) previous).refresh();
                    }
                    previous.showBack();
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = CategorieService.getInstance().edit(
                            new Categorie(
                                    currentCategorie.getId(),
                                    nomCTF.getText(),
                                    typeTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Categorie modifié avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de categorie. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    ((DisplayAll) previous).refresh();
                    previous.showBack();
                }
            });
        }
    }

    private boolean controleDeSaisie() {

        if (nomCTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nom", new Command("Ok"));
            return false;
        }
        if (typeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le type", new Command("Ok"));
            return false;
        }

        return true;
    }
}
