package com.easyFit.gui.back.user;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.User;
import com.easyFit.services.UserService;

import java.util.ArrayList;

public class Manage extends Form {

    User currentUser;

    Label emailLabel, usernameLabel, passwordLabel, rolesLabel;
    TextField emailTF, usernameTF, passwordTF, rolesTF;
    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentUser == null ? "Ajouter utilisateur" : "Modifier utilisateur", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentUser = DisplayAll.currentUser;

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
        passwordTF = new TextField();
        passwordTF.setHint("Tapez le mot de passe");

        rolesLabel = new Label("Role : ");
        rolesLabel.setUIID("labelDefault");
        rolesTF = new TextField();
        rolesTF.setHint("Tapez le role");


        if (currentUser == null) {
            manageButton = new Button("Ajouter");
        } else {

            emailTF.setText(currentUser.getEmail());
            usernameTF.setText(currentUser.getUsername());
            passwordTF.setText(currentUser.getPassword());
            rolesTF.setText(currentUser.getRoles().get(0));

            manageButton = new Button("Modifier");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                emailLabel, emailTF,
                usernameLabel, usernameTF,
                passwordLabel, passwordTF,
                rolesLabel, rolesTF,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
        if (currentUser == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    ArrayList roles = new ArrayList<String>();
                    roles.add(rolesTF.getText());
                    int responseCode = UserService.getInstance().add(

                            new User(
                                    emailTF.getText(),
                                    usernameTF.getText(),
                                    passwordTF.getText(),
                                    passwordTF.getText(),
                                    roles
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "User ajouté avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de user. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    ArrayList roles = new ArrayList<String>();
                    roles.add(rolesTF.getText());
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
                    if (responseCode == 200) {
                        Dialog.show("Succés", "User modifié avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de user. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        }
    }

    private void showBackAndRefresh() {
        ((DisplayAll) previous).refresh();
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

        if (rolesTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le role", new Command("Ok"));
            return false;
        }

        return true;
    }
}