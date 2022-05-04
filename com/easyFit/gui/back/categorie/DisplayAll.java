package com.easyFit.gui.back.categorie;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.Categorie;
import com.easyFit.services.CategorieService;

import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Categorie currentCategorie = null;
    Button addBtn;
    Label nomCLabel, typeLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Categories", new BoxLayout(BoxLayout.Y_AXIS));

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
        addBtn.addActionListener(action -> {
            currentCategorie = null;
            new Manage(this, false).show();
        });
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

        editBtn = new Button("Modifier");
        editBtn.addActionListener(action -> {
            currentCategorie = categorie;
            new Manage(this, false).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.addActionListener(l -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer cette categorie ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = CategorieService.getInstance().delete(categorie.getId());

                if (responseCode == 200) {
                    currentCategorie = null;
                    dlg.dispose();
                    categorieModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression de la categorie. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        categorieModel.addAll(nomCLabel, typeLabel, btnsContainer);

        return categorieModel;
    }
}