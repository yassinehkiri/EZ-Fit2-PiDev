package com.easyFit.gui.back;

import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.MainApp;
import com.easyFit.gui.Login;

public class AccueilBack extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");
    Label label;

    public AccueilBack() {
        super("Espace Admin", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
    }

    private void addGUIs() {
        ImageViewer userImage = new ImageViewer(theme.getImage("person.jpg").fill(200, 200));
        userImage.setUIID("candidatImage");
        if (MainApp.getSession() != null) {
            label = new Label(MainApp.getSession().getEmail());
        } else {
            label = new Label("Admin");
        }
        label.setUIID("links");


        Container userContainer = new Container(new BorderLayout());
        userContainer.setUIID("userContainer");
        userContainer.add(BorderLayout.WEST, userImage);
        userContainer.add(BorderLayout.CENTER, label);

        Button btnDeconnexion = new Button("Deconnexion");
        btnDeconnexion.setUIID("buttonLogout");
        btnDeconnexion.setMaterialIcon(FontImage.MATERIAL_LOGOUT);
        btnDeconnexion.addActionListener(action -> {
            MainApp.setSession(null);
            Login.loginForm.showBack();
        });

        Container menuContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        menuContainer.addAll(
                userContainer,
                makeUsersButton(),
                makeAbonnementsButton(),
                makeAbonneButton(),
                makePublicationsButton(),
                makeReclamationButton(),
                makeCommentairesButton(),
                makeProduitsButton(),
                makeCategoriesButton(),
                makeOrderButton(),
                makeFournisseursButton(),
                makeEquipementButton(),
                makeLivraisonButton(),
                makePersonnelButton(),
                makeSalleButton(),
                makePermissionButton(),
                btnDeconnexion
        );
        this.add(menuContainer);
    }

    private Button makeUsersButton() {
        Button button = new Button("    Users");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_PERSON);
        button.addActionListener(action -> new com.easyFit.gui.back.user.DisplayAll(this).show());
        return button;
    }

    private Button makeAbonnementsButton() {
        Button button = new Button("    Abonnements");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_SUBSCRIPTIONS);
        button.addActionListener(action -> new com.easyFit.gui.back.abonnement.DisplayAll(this).show());
        return button;
    }

    private Button makeAbonneButton() {
        Button button = new Button("    Abonnés");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_PERSON_ADD);
        button.addActionListener(action -> new com.easyFit.gui.back.abonne.DisplayAll(this).show());
        return button;
    }

    private Button makeReclamationButton() {
        Button button = new Button("    Reclamations");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_BUS_ALERT);
        button.addActionListener(action -> new com.easyFit.gui.back.reclamation.DisplayAll(this).show());
        return button;
    }

    private Button makePublicationsButton() {
        Button button = new Button("    Publications");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_POST_ADD);
        button.addActionListener(action -> new com.easyFit.gui.back.post.DisplayAll(this).show());
        return button;
    }

    private Button makeCommentairesButton() {
        Button button = new Button("    Commentaires");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_COMMENT);
        button.addActionListener(action -> new com.easyFit.gui.back.comment.DisplayAll(this).show());
        return button;
    }

    private Button makeProduitsButton() {
        Button button = new Button("    Produits");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_PRODUCTION_QUANTITY_LIMITS);
        button.addActionListener(action -> new com.easyFit.gui.back.produit.DisplayAll(this).show());
        return button;
    }

    private Button makeCategoriesButton() {
        Button button = new Button("    Categories produit");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_BOOKMARK);
        button.addActionListener(action -> new com.easyFit.gui.back.categorie.DisplayAll(this).show());
        return button;
    }

    private Button makeOrderButton() {
        Button button = new Button("    Order Detail");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_REORDER);
        button.addActionListener(action -> new com.easyFit.gui.back.orderDetail.DisplayAll(this).show());
        return button;
    }

    private Button makeFournisseursButton() {
        Button button = new Button("    Fournisseur");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_PERSON_ADD_ALT);
        button.addActionListener(action -> new com.easyFit.gui.back.fournisseur.DisplayAll(this).show());
        return button;
    }

    private Button makeEquipementButton() {
        Button button = new Button("    Equipement");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_PAN_TOOL);
        button.addActionListener(action -> new com.easyFit.gui.back.equipement.DisplayAll(this).show());
        return button;
    }

    private Button makeLivraisonButton() {
        Button button = new Button("    Livraison");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_DELIVERY_DINING);
        button.addActionListener(action -> new com.easyFit.gui.back.livraison.DisplayAll(this).show());
        return button;
    }

    private Button makePersonnelButton() {
        Button button = new Button("    Personnel");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_PERSON_ADD);
        button.addActionListener(action -> new com.easyFit.gui.back.personnel.DisplayAll(this).show());
        return button;
    }

    private Button makeSalleButton() {
        Button button = new Button("    Salles");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_MEETING_ROOM);
        button.addActionListener(action -> new com.easyFit.gui.back.salle.DisplayAll(this).show());
        return button;
    }

    private Button makePermissionButton() {
        Button button = new Button("    Permissions");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_VPN_KEY);
        button.addActionListener(action -> new com.easyFit.gui.back.permission.DisplayAll(this).show());
        return button;
    }
}
