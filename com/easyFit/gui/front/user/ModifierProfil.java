package com.easyFit.gui.front.user;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.MainApp;
import com.easyFit.entities.User;
import com.easyFit.gui.front.AccueilFront;
import com.easyFit.services.UserService;

import java.util.ArrayList;

public class ModifierProfil extends Form {

    User currentUser;

    Label emailLabel, usernameLabel, passwordLabel;
    TextField emailTF, usernameTF, passwordTF;
    Button manageButton;

    Form previous;

    public ModifierProfil(Form previous) {
        super("Modifier profil", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentUser = Profil.currentUser;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        emailLabel = new Label("Email : ");
        emailTF = new TextField();
        emailTF.setHint("Tapez l'email");

        usernameLabel = new Label("Username : ");
        usernameLabel.setUIID("labelDefault");
        usernameTF = new TextField();
        usernameTF.setHint("Tapez le username");

        passwordLabel = new Label("Mot de passe : ");
        passwordLabel.setUIID("labelDefault");
        passwordTF = new TextField("", "Tapez votre mot de passe", 20, TextField.PASSWORD);


        if (currentUser == null) {
            manageButton = new Button("Ajouter");
        } else {

            emailTF.setText(currentUser.getEmail());
            usernameTF.setText(currentUser.getUsername());

            manageButton = new Button("Modifier");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                emailLabel, emailTF,
                usernameLabel, usernameTF,
                passwordLabel, passwordTF,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
        manageButton.addActionListener(action -> {
            if (controleDeSaisie()) {
                ArrayList roles = new ArrayList<String>();
                roles.add("ROLE_USER");
                int responseCode = UserService.getInstance().edit(
                        new User(
                                currentUser.getId(),
                                emailTF.getText(),
                                usernameTF.getText(),
                                passwordTF.getText(),
                                passwordTF.getText(),
                                roles
                        )
                );
                MainApp.setSession(new User(
                        currentUser.getId(),
                        emailTF.getText(),
                        usernameTF.getText(),
                        passwordTF.getText(),
                        passwordTF.getText(),
                        roles
                ));
                if (responseCode == 200) {
                    Dialog.show("Succés", "User modifié avec succes", new Command("Ok"));
                } else {
                    Dialog.show("Erreur", "Erreur de modification de user. Code d'erreur : " + responseCode, new Command("Ok"));
                }

                showBackAndRefresh();
            }
        });
    }

    private void showBackAndRefresh() {
        ((AccueilFront) AccueilFront.accueilFrontForm).refreshUsername();
        ((Profil) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (emailTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir l'email", new Command("Ok"));
            return false;
        }

        if (usernameTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le username", new Command("Ok"));
            return false;
        }

        if (passwordTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le mot de passe", new Command("Ok"));
            return false;
        }

        return true;
    }
}