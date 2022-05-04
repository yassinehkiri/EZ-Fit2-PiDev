package com.easyFit.gui;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.User;
import com.easyFit.services.UserService;

import java.util.ArrayList;

public class Register extends Form {

    Label emailLabel, usernameLabel, passwordLabel, passwordConfirmationLabel;
    TextField emailTF, usernameTF, passwordTF, passwordConfirmationTF;
    Label loginLabel;
    Button registerButton, loginButton;

    public Register() {
        super("Inscription", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
        addActions();
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

        passwordConfirmationLabel = new Label("Mot de passe : ");
        passwordConfirmationLabel.setUIID("labelDefault");
        passwordConfirmationTF = new TextField("", "Retapez votre mot de passe", 20, TextField.PASSWORD);

        registerButton = new Button("S'inscrire");

        loginLabel = new Label("Vous avez deja un compte ?");
        loginButton = new Button("Connexion");

        this.addAll(
                emailLabel, emailTF,
                usernameLabel, usernameTF,
                passwordLabel, passwordTF,
                passwordConfirmationLabel, passwordConfirmationTF,
                registerButton,
                loginLabel, loginButton
        );
    }

    private void addActions() {
        registerButton.addActionListener(l -> {
            if (controleDeSaisie()) {
                ArrayList<String> roles = new ArrayList<>();
                roles.add("ROLE_USER");
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
                    Dialog.show("Succés", "Inscription effectué avec succes", new Command("Ok"));
                    Login.loginForm.showBack();
                } else {
                    Dialog.show("Erreur", "Cet email est deja utilisé : " + responseCode, new Command("Ok"));
                }
            }
        });

        loginButton.addActionListener(l -> Login.loginForm.showBack());
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

        if (passwordConfirmationTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir la confirmation", new Command("Ok"));
            return false;
        }

        if (!passwordConfirmationTF.getText().equals(passwordTF.getText())) {
            Dialog.show("Avertissement", "Mot de passe et confirmation doivent etre identiques", new Command("Ok"));
            return false;
        }

        return true;
    }
}
