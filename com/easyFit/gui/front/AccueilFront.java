package com.easyFit.gui.front;

import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.MainApp;
import com.easyFit.gui.Login;
import com.easyFit.gui.front.user.Profil;

public class AccueilFront extends Form {

    public static Form accueilFrontForm;
    Resources theme = UIManager.initFirstTheme("/theme");
    Label label;

    public AccueilFront() {
        super(new BorderLayout());
        accueilFrontForm = this;
        addGUIs();
    }

    public void refreshUsername() {
        label.setText("Username : " + MainApp.getSession().getUsername());
    }

    private void addGUIs() {
        Tabs tabs = new Tabs();

        tabs.addTab("Publications", FontImage.MATERIAL_POST_ADD, 5,
                new com.easyFit.gui.front.post.DisplayAll()
        );
        tabs.addTab("Produits", FontImage.MATERIAL_LOCAL_GROCERY_STORE, 5,
                new com.easyFit.gui.front.produit.DisplayAll()
        );
        tabs.addTab("Equipement", FontImage.MATERIAL_ADD_SHOPPING_CART, 5,
                new com.easyFit.gui.front.equipement.DisplayAll()
        );
        tabs.addTab("Plus", FontImage.MATERIAL_MENU, 5, moreGUI());

        this.add(BorderLayout.CENTER, tabs);
    }

    private Container moreGUI() {

        ImageViewer userImage = new ImageViewer(theme.getImage("person.jpg").fill(200, 200));
        userImage.setUIID("candidatImage");
        label = new Label("Username : " + MainApp.getSession().getUsername());
        label.setUIID("links");
        Button btnDeconnexion = new Button();
        btnDeconnexion.setUIID("buttonLogout");
        btnDeconnexion.setMaterialIcon(FontImage.MATERIAL_ARROW_FORWARD);
        btnDeconnexion.setText("    Deconnexion");
        btnDeconnexion.setTextPosition(LEFT);
        btnDeconnexion.addActionListener(action -> {
            MainApp.setSession(null);
            Login.loginForm.showBack();
        });

        Button profilButton = new Button("Mon profil");
        profilButton.addActionListener(action -> new Profil(AccueilFront.accueilFrontForm).show());

        Container userContainer = new Container(new BorderLayout());
        userContainer.setUIID("userContainer");
        userContainer.add(BorderLayout.EAST, profilButton);
        userContainer.add(BorderLayout.WEST, userImage);
        userContainer.add(BorderLayout.CENTER, label);

        Container menuContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        menuContainer.addAll(
                userContainer,
                makeCategoriesButton(),
                makeOrderButton(),
                makeLivraisonButton(),
                makeReclamationButton(),
                makeSalleButton(),
                makePermissionButton(),
                btnDeconnexion
        );

        return (menuContainer);
    }

    private Button makeCategoriesButton() {
        Button button = new Button("    Categories produit");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_BOOKMARK);
        button.addActionListener(action -> new com.easyFit.gui.front.categorie.DisplayAll(this).show());
        return button;
    }

    private Button makeLivraisonButton() {
        Button button = new Button("    Livraisons");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_DELIVERY_DINING);
        button.addActionListener(action -> new com.easyFit.gui.front.livraison.DisplayAll(this).show());
        return button;
    }

    private Button makeOrderButton() {
        Button button = new Button("    Order Detail");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_REORDER);
        button.addActionListener(action -> new com.easyFit.gui.front.orderDetail.DisplayAll(this).show());
        return button;
    }

    private Button makeReclamationButton() {
        Button button = new Button("    Mes reclamations");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_PERSONAL_INJURY);
        button.addActionListener(action -> new com.easyFit.gui.front.reclamation.DisplayAll(this).show());
        return button;
    }

    private Button makeSalleButton() {
        Button button = new Button("    Salles");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_ROOM);
        button.addActionListener(action -> new com.easyFit.gui.front.salle.DisplayAll(this).show());
        return button;
    }

    private Button makePermissionButton() {
        Button button = new Button("    Mes permissions");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_VPN_KEY);
        button.addActionListener(action -> new com.easyFit.gui.front.permission.DisplayAll(this).show());
        return button;
    }
}
