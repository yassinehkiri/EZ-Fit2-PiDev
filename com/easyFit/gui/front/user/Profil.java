package com.easyFit.gui.front.user;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.MainApp;
import com.easyFit.entities.User;
import com.easyFit.services.UserService;

public class Profil extends Form {

    public static User currentUser = null;
    Button editProfileBTN;
    Label emailLabel, usernameLabel, passwordLabel, rolesLabel;

    public Profil(Form previous) {
        super("Mon profil", new BoxLayout(BoxLayout.Y_AXIS));

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

        currentUser = UserService.getInstance().getUserById(MainApp.getSession().getId());
        this.add(makeUserModel(currentUser));

        editProfileBTN = new Button("Modifier mon profil");
        this.add(editProfileBTN);
    }

    private void addActions() {
        editProfileBTN.addActionListener(action -> {
            new ModifierProfil(this).show();
        });
    }

    private Component makeUserModel(User user) {
        Container userModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        userModel.setUIID("containerRounded");

        emailLabel = new Label("Email : " + user.getEmail());
        emailLabel.setUIID("labelDefault");

        usernameLabel = new Label("Username : " + user.getUsername());
        usernameLabel.setUIID("labelDefault");

        passwordLabel = new Label("Mot de passe : " + user.getPassword());
        passwordLabel.setUIID("labelDefault");

        rolesLabel = new Label("Roles : " + user.getRoles());
        rolesLabel.setUIID("labelDefault");

        userModel.addAll(
                emailLabel, usernameLabel, passwordLabel, rolesLabel
        );

        return userModel;
    }
}