package com.easyFit.gui.front.orderDetail;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.OrderDetail;
import com.easyFit.entities.Produit;
import com.easyFit.services.OrderDetailService;

public class Manage extends Form {


    public static Produit selectedProduit;
    OrderDetail currentOrderDetail;
    Label quantityLabel, prixLabel, produitLabel;
    TextField
            quantityTF,
            prixTF,
            produitTF, elemTF;


    Label selectedProduitLabel;
    Button selectProduitButton;


    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentOrderDetail == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;


        selectedProduit = null;

        currentOrderDetail = DisplayAll.currentOrderDetail;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }


    public void refreshProduit() {
        selectedProduitLabel.setText(selectedProduit.getNomP());
        selectProduitButton.setText("Choisir un produit");
        this.refreshTheme();
    }


    private void addGUIs() {


        quantityLabel = new Label("Quantity : ");
        quantityLabel.setUIID("labelDefault");
        quantityTF = new TextField();
        quantityTF.setHint("Tapez le quantity");

        prixLabel = new Label("Prix : ");
        prixLabel.setUIID("labelDefault");
        prixTF = new TextField();
        prixTF.setHint("Tapez le prix");

        produitLabel = new Label("Produit : ");
        produitLabel.setUIID("labelDefault");
        produitTF = new TextField();
        produitTF.setHint("Tapez le produit");


        produitLabel = new Label("produit : ");
        produitLabel.setUIID("labelDefault");
        selectedProduitLabel = new Label("null");
        selectProduitButton = new Button("Choisir produit");
        selectProduitButton.addActionListener(l -> new ChooseProduit(this).show());


        if (currentOrderDetail == null) {


            manageButton = new Button("Ajouter");
        } else {


            quantityTF.setText(String.valueOf(currentOrderDetail.getQuantity()));
            prixTF.setText(String.valueOf(currentOrderDetail.getPrix()));

            selectedProduit = currentOrderDetail.getProduit();

            produitLabel = new Label("produit : ");
            produitLabel.setUIID("labelDefault");
            selectedProduitLabel = new Label("null");
            selectedProduitLabel.setText(selectedProduit.getNomP());
            selectProduitButton.setText("Choisir un produit");


            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonMain");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(

                quantityLabel, quantityTF, prixLabel, prixTF, produitLabel,
                selectedProduitLabel, selectProduitButton,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentOrderDetail == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = OrderDetailService.getInstance().add(
                            new OrderDetail(


                                    (int) Float.parseFloat(quantityTF.getText()),
                                    Float.parseFloat(prixTF.getText()),
                                    selectedProduit
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "OrderDetail ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de orderDetail. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = OrderDetailService.getInstance().edit(
                            new OrderDetail(
                                    currentOrderDetail.getId(),


                                    (int) Float.parseFloat(quantityTF.getText()),
                                    Float.parseFloat(prixTF.getText()),
                                    selectedProduit

                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "OrderDetail modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de orderDetail. Code d'erreur : " + responseCode, new Command("Ok"));
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


        if (quantityTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le quantity", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(quantityTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", quantityTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }


        if (prixTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le prix", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(prixTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", prixTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }


        if (selectedProduit == null) {
            Dialog.show("Avertissement", "Veuillez choisir un produit", new Command("Ok"));
            return false;
        }


        return true;
    }
}