package com.easyFit.gui.front.salle;

import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.entities.Salle;
import com.easyFit.gui.front.AccueilFront;
import com.easyFit.services.SalleService;
import com.easyFit.utils.Statics;

import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Salle currentSalle = null;
    Resources theme = UIManager.initFirstTheme("/theme");
    Label nomLabel, adresseLabel, codePostalLabel, villeLabel, nombreLabel, imageLabel, longitudeLabel, lattitudeLabel;
    ImageViewer imageIV;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Salles", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }
    
    TextField searchTF;
    ArrayList<Component> componentModels;

    private void addGUIs() {
        ArrayList<Salle> listSalles = SalleService.getInstance().getAll();
        componentModels = new ArrayList<>();

        searchTF = new TextField("", "Chercher une salle par nom");
        searchTF.addDataChangedListener((d, t) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    this.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            for (Salle salle : listSalles) {
                if (salle.getNom().startsWith(searchTF.getText())) {
                    Component model = makeSalleModel(salle);
                    this.add(model);
                    componentModels.add(model);
                }
            }
            this.revalidate();
        });
        this.add(searchTF);
        
        if (listSalles.size() > 0) {
            for (Salle salle : listSalles) {
                Component model = makeSalleModel(salle);
                this.add(model);
                componentModels.add(model);
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

        longitudeLabel = new Label("Longitude : " + salle.getLongitude());
        longitudeLabel.setUIID("labelDefault");

        lattitudeLabel = new Label("Lattitude : " + salle.getLattitude());
        lattitudeLabel.setUIID("labelDefault");

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

        salleModel.addAll(
                imageIV,
                nomLabel, adresseLabel, codePostalLabel, villeLabel, nombreLabel, longitudeLabel, lattitudeLabel
        );

        try {
            float longitude = Float.parseFloat(salle.getLongitude());
            float lattitude = Float.parseFloat(salle.getLattitude());

            Button mapBtn = new Button("Afficher dans maps");
            mapBtn.addActionListener(l -> {
                new MapForm(AccueilFront.accueilFrontForm, "Salle : " + salle.getNom(), longitude, lattitude).show();
            });
            salleModel.add(mapBtn);
        } catch (NumberFormatException | NullPointerException e) {

        }

        return salleModel;
    }
}
