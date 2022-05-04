package com.easyFit.gui.back.produit;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.Categorie;
import com.easyFit.services.CategorieService;

import java.util.ArrayList;

public class ChooseCategorie extends Form {

    Form previousForm;
    Button addBtn;
    Label nomCLabel, typeLabel;
    Button chooseBtn;
    Container btnsContainer;

    public ChooseCategorie(Form previous) {
        super("Choisir un categorie", new BoxLayout(BoxLayout.Y_AXIS));

        previousForm = previous;
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
        addBtn = new Button("Nouvelle categorie");
        this.add(addBtn);

        ArrayList<Categorie> listCategories = CategorieService.getInstance().getAll();
        if (listCategories.size() > 0) {
            for (Categorie categories : listCategories) {
                this.add(makeCategorieModel(categories));
            }
        } else {
            this.add(new Label("Aucune categorie"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> new com.easyFit.gui.back.categorie.Manage(this, true).show());
    }

    private Component makeCategorieModel(Categorie categorie) {
        Container categorieModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        categorieModel.setUIID("containerRounded");

        nomCLabel = new Label("Nom : " + categorie.getNomC());
        nomCLabel.setUIID("labelDefault");

        typeLabel = new Label("Type : " + categorie.getType());
        typeLabel.setUIID("labelDefault");

        chooseBtn = new Button("Choose");
        chooseBtn.addActionListener(l -> {
            Manage.selectedCategorie = categorie;
            ((Manage) previousForm).refreshCategorie();
            previousForm.showBack();
        });

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        categorieModel.addAll(nomCLabel, typeLabel, btnsContainer);

        return categorieModel;
    }
}