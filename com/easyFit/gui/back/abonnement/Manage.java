package com.easyFit.gui.back.abonnement;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.Abonnement;
import com.easyFit.services.AbonnementService;

public class Manage extends Form {


    Abonnement currentAbonnement;


    Label typeLabel, tarifLabel, dateDebutLabel, dateFinLabel;
    TextField
            typeTF,
            tarifTF, elemTF;
    PickerComponent dateDebutTF;
    PickerComponent dateFinTF;


    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentAbonnement == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;


        currentAbonnement = DisplayAll.currentAbonnement;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }


    private void addGUIs() {


        typeLabel = new Label("Type : ");
        typeLabel.setUIID("labelDefault");
        typeTF = new TextField();
        typeTF.setHint("Tapez le type");

        tarifLabel = new Label("Tarif : ");
        tarifLabel.setUIID("labelDefault");
        tarifTF = new TextField();
        tarifTF.setHint("Tapez le tarif");
        dateDebutTF = PickerComponent.createDate(null).label("DateDebut");
        dateFinTF = PickerComponent.createDate(null).label("DateFin");


        if (currentAbonnement == null) {


            manageButton = new Button("Ajouter");
        } else {


            typeTF.setText(currentAbonnement.getType());
            tarifTF.setText(String.valueOf(currentAbonnement.getTarif()));
            dateDebutTF.getPicker().setDate(currentAbonnement.getDateDebut());
            dateFinTF.getPicker().setDate(currentAbonnement.getDateFin());


            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonMain");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(

                typeLabel, typeTF, tarifLabel, tarifTF, dateDebutTF, dateFinTF,

                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentAbonnement == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = AbonnementService.getInstance().add(
                            new Abonnement(


                                    typeTF.getText(),
                                    (int) Float.parseFloat(tarifTF.getText()),
                                    dateDebutTF.getPicker().getDate(),
                                    dateFinTF.getPicker().getDate()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Abonnement ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de abonnement. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = AbonnementService.getInstance().edit(
                            new Abonnement(
                                    currentAbonnement.getId(),


                                    typeTF.getText(),
                                    (int) Float.parseFloat(tarifTF.getText()),
                                    dateDebutTF.getPicker().getDate(),
                                    dateFinTF.getPicker().getDate()

                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Abonnement modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de abonnement. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        }
    }

    private void showBackAndRefresh() {
        ((DisplayAll) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {


        if (typeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le type", new Command("Ok"));
            return false;
        }


        if (tarifTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le tarif", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(tarifTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", tarifTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }


        if (dateDebutTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la dateDebut", new Command("Ok"));
            return false;
        }


        if (dateFinTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la dateFin", new Command("Ok"));
            return false;
        }


        return true;
    }
}