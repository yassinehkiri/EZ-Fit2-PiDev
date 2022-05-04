package com.easyFit.gui.front.permission;

import com.codename1.components.ImageViewer;
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
    Label typeLabel, reclamationLabel, dateDebutLabel, dateFinLabel, personnelLabel;
    ImageViewer imageIV;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Permissions", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {
        ArrayList<Permission> listPermissions = PermissionService.getInstance().getAll();

        if (listPermissions.size() > 0) {
            for (Permission permission : listPermissions) {
                Component model = makePermissionModel(permission);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
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

        permissionModel.addAll(
                imageIV,
                typeLabel, reclamationLabel, dateDebutLabel, dateFinLabel, personnelLabel,
                btnsContainer
        );

        return permissionModel;
    }
}