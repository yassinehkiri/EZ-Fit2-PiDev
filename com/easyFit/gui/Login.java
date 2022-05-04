package com.easyFit.gui;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.MainApp;
import com.easyFit.entities.User;
import com.easyFit.services.UserService;

import java.util.List;

public class Login extends Form {

    public static Form loginForm;
    TextField tfEmail;
    TextField tfPassword;
    Button btnConnexion, btnInscription;
    Button backendBtn;

    public Login() {
        super("Connexion", new BoxLayout(BoxLayout.Y_AXIS));
        loginForm = this;
        addGUIs();
        addActions();
    }

    private void addGUIs() {

        tfEmail = new TextField("", "Entrez votre email");
        tfPassword = new TextField("", "Tapez votre mot de passe", 20, TextField.PASSWORD);
        btnConnexion = new Button("Connexion");
        btnInscription = new Button("Inscription");

        backendBtn = new Button("Backend");
        backendBtn.setUIID("button");

        this.addAll(tfEmail, tfPassword, btnConnexion, new Label("Besoin d'un compte ?"), btnInscription, backendBtn);
    }

    private void addActions() {
        btnConnexion.addActionListener(action -> {
            User user = UserService.getInstance().verifierMotDePasse(tfEmail.getText(), tfPassword.getText());
            if (user != null) {
                login(user);
            } else {
                Dialog.show("Erreur", "Identifiants invalides", new Command("Ok"));
            }
        });

        btnInscription.addActionListener(action -> new Register().show());
        backendBtn.addActionListener(l -> new com.easyFit.gui.back.AccueilBack().show());
    }

    private void login(User user) {

        MainApp.setSession(user);

        List<String> roles = user.getRoles();
        
        boolean isAdmin = false;
        for (String role : roles) {
            if ("ROLE_ADMIN".equals(role)) {
                isAdmin = true;
                break;
            }
        }

        if (isAdmin) {
            new com.easyFit.gui.back.AccueilBack().show();
        } else {
            new com.easyFit.gui.front.AccueilFront().show();
        }
    }
}
