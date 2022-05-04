package com.easyFit.gui.front.categorie;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.Categorie;
import com.easyFit.services.CategorieService;

import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Categorie currentCategorie = null;
    Label nomCLabel, typeLabel;
    Button chooseBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Categories", new BoxLayout(BoxLayout.Y_AXIS));

        addGUIs();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {
        ArrayList<Categorie> listCategories = CategorieService.getInstance().getAll();
        if (listCategories.size() > 0) {
            for (Categorie categories : listCategories) {
                this.add(makeCategorieModel(categories));
            }
        } else {
            this.add(new Label("Aucune categorie"));
        }
    }

    private Component makeCategorieModel(Categorie categorie) {
        Container categorieModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        categorieModel.setUIID("containerRounded");

        nomCLabel = new Label("Nom : " + categorie.getNomC());
        nomCLabel.setUIID("labelDefault");

        typeLabel = new Label("Type : " + categorie.getType());
        typeLabel.setUIID("labelDefault");

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        chooseBtn = new Button("Afficher Produits");
        chooseBtn.addActionListener(l -> new DisplayProdByCat(this, categorie.getId()).show());

        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        categorieModel.addAll(nomCLabel, typeLabel, btnsContainer);

        return categorieModel;
    }
}