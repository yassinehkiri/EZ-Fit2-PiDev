package com.easyFit.gui.back.permission;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.entities.Permission;
import com.easyFit.services.PermissionService;
import com.easyFit.utils.Statics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {


    public static Permission currentPermission = null;
    Resources theme = UIManager.initFirstTheme("/theme");
    Button addBtn;
    Label typeLabel, reclamationLabel, dateDebutLabel, dateFinLabel, personnelLabel, imageLabel;
    ImageViewer imageIV;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Permissions", new BoxLayout(BoxLayout.Y_AXIS));

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

        ArrayList<Permission> listPermissions = PermissionService.getInstance().getAll();
        if (listPermissions.size() > 0) {
            for (Permission listPermission : listPermissions) {
                this.add(makePermissionModel(listPermission));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentPermission = null;
            new Manage(this).show();
        });
    }

    private Component makePermissionModel(Permission permission) {
        Container permissionModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        permissionModel.setUIID("containerRounded");


        typeLabel = new Label("Type : " + permission.getType());

        typeLabel.setUIID("labelDefault");


        reclamationLabel = new Label("Reclamation : " + permission.getReclamation());

        reclamationLabel.setUIID("labelDefault");


        dateDebutLabel = new Label("DateDebut : " + new SimpleDateFormat("dd-MM-yyyy").format(permission.getDateDebut()));

        dateDebutLabel.setUIID("labelDefault");


        dateFinLabel = new Label("DateFin : " + new SimpleDateFormat("dd-MM-yyyy").format(permission.getDateFin()));

        dateFinLabel.setUIID("labelDefault");


        personnelLabel = new Label("Personnel : " + permission.getPersonnel());

        personnelLabel.setUIID("labelDefault");


        imageLabel = new Label("Image : " + permission.getImage());

        imageLabel.setUIID("labelDefault");


        personnelLabel = new Label("Personnel : " + permission.getPersonnel().getNom());
        personnelLabel.setUIID("labelDefault");


        if (permission.getImage() != null) {
            String url = Statics.PERMISSION_IMAGE_URL + permission.getImage();
            Image image = URLImage.createToStorage(
                    EncodedImage.createFromImage(theme.getImage("default.jpg").fill(500, 500), false),
                    url,
                    url,
                    URLImage.RESIZE_SCALE
            );
            imageIV = new ImageViewer(image);
        } else {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(500, 500));
        }
        imageIV.setFocusable(false);


        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonMain");
        editBtn.addActionListener(action -> {
            currentPermission = permission;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce permission ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = PermissionService.getInstance().delete(permission.getId());

                if (responseCode == 200) {
                    currentPermission = null;
                    dlg.dispose();
                    permissionModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du permission. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.CENTER, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        permissionModel.addAll(
                imageLabel,
                imageIV,
                typeLabel, reclamationLabel, dateDebutLabel, dateFinLabel, personnelLabel,
                btnsContainer
        );

        return permissionModel;
    }
}