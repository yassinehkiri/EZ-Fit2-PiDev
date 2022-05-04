package com.easyFit.gui.back.orderDetail;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.OrderDetail;
import com.easyFit.services.OrderDetailService;

import java.util.ArrayList;

public class DisplayAll extends Form {


    public static OrderDetail currentOrderDetail = null;
    Button addBtn;
    Label quantityLabel, prixLabel, produitLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("OrderDetails", new BoxLayout(BoxLayout.Y_AXIS));

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
        addBtn = new Button("Ajouter");
        addBtn.setUIID("buttonWhiteCenter");

        this.add(addBtn);

        ArrayList<OrderDetail> listOrderDetails = OrderDetailService.getInstance().getAll();
        if (listOrderDetails.size() > 0) {
            for (OrderDetail listOrderDetail : listOrderDetails) {
                this.add(makeOrderDetailModel(listOrderDetail));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentOrderDetail = null;
            new Manage(this).show();
        });
    }

    private Component makeOrderDetailModel(OrderDetail orderDetail) {
        Container orderDetailModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        orderDetailModel.setUIID("containerRounded");


        quantityLabel = new Label("Quantity : " + orderDetail.getQuantity());

        quantityLabel.setUIID("labelDefault");


        prixLabel = new Label("Prix : " + orderDetail.getPrix());

        prixLabel.setUIID("labelDefault");

        produitLabel = new Label("Produit : " + orderDetail.getProduit().getNomP() + "/ Prix : " + orderDetail.getProduit().getPrix());
        produitLabel.setUIID("labelDefault");

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonMain");
        editBtn.addActionListener(action -> {
            currentOrderDetail = orderDetail;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce orderDetail ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = OrderDetailService.getInstance().delete(orderDetail.getId());

                if (responseCode == 200) {
                    currentOrderDetail = null;
                    dlg.dispose();
                    orderDetailModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du orderDetail. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        float total = orderDetail.getProduit().getPrix() * orderDetail.getQuantity();
        Button calculSommeButton = new Button("Prix total");
        calculSommeButton.addActionListener(l -> Dialog.show("Total", "Le prix total est : " + total, new Command("Ok")));

        btnsContainer.add(BorderLayout.WEST, calculSommeButton);
        btnsContainer.add(BorderLayout.CENTER, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        orderDetailModel.addAll(
                produitLabel,
                quantityLabel,
                btnsContainer
        );

        return orderDetailModel;
    }
}