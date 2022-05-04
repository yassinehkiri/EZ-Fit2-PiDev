package com.easyFit.gui.back.personnel;

import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.entities.Salle;
import com.easyFit.services.SalleService;
import com.easyFit.utils.Statics;

import java.util.ArrayList;

public class ChooseSalle extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");

    Form previousForm;
    Label nomLabel, adresseLabel, codePostalLabel, villeLabel, nombreLabel, imageLabel;
    Button chooseBtn;
    ImageViewer imageIV;

    Container btnsContainer;

    public ChooseSalle(Form previous) {
        super("Choisir un salle", new BoxLayout(BoxLayout.Y_AXIS));

        previousForm = previous;
        addGUIs();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        this.refreshTheme();
    }

    private void addGUIs() {
        ArrayList<Salle> listSalles = SalleService.getInstance().getAll();
        if (listSalles.size() > 0) {
            for (Salle salles : listSalles) {
                this.add(makeSalleModel(salles));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private Component makeSalleModel(Salle salle) {
        Container salleModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        salleModel.setUIID("containerRounded");

        nomLabel = new Label("Nom : " + salle.getNom());

        nomLabel.setUIID("labelDefault");


        adresseLabel = new Label("Adresse : " + salle.getAdresse());

        adresseLabel.setUIID("labelDefault");


        codePostalLabel = new Label("CodePostal : " + salle.getCodePostal());

        codePostalLabel.setUIID("labelDefault");


        villeLabel = new Label("Ville : " + salle.getVille());
        villeLabel.setUIID("labelDefault");

        nombreLabel = new Label("Nombre : " + salle.getNombre());
        nombreLabel.setUIID("labelDefault");

        imageLabel = new Label("Image : " + salle.getImage());
        imageLabel.setUIID("labelDefault");


        if (salle.getImage() != null) {
            String url = Statics.SALLE_IMAGE_URL + salle.getImage();
            Image image = URLImage.createToStorage(
                    EncodedImage.createFromImage(theme.getImage("default.jpg").fill(700, 500), false),
                    url,
                    url,
                    URLImage.RESIZE_SCALE
            );
            imageIV = new ImageViewer(image);
        } else {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(700, 500));
        }
        imageIV.setFocusable(false);
        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            Manage.selectedSalle = salle;
            ((Manage) previousForm).refreshSalle();
            previousForm.showBack();
        });

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        salleModel.addAll(
                nomLabel, adresseLabel, codePostalLabel, villeLabel, nombreLabel,
                imageIV,
                btnsContainer
        );

        return salleModel;
    }
}