package com.easyFit.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.easyFit.entities.Permission;
import com.easyFit.entities.Personnel;
import com.easyFit.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PermissionService {

    public static PermissionService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Permission> listPermissions;

    private PermissionService() {
        cr = new ConnectionRequest();
    }

    public static PermissionService getInstance() {
        if (instance == null) {
            instance = new PermissionService();
        }
        return instance;
    }

    public ArrayList<Permission> getAll() {
        listPermissions = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/permission");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listPermissions = getList();
                }

                cr.removeResponseListener(this);
            }
        });

        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listPermissions;
    }

    private ArrayList<Permission> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Permission permission = new Permission(
                        (int) Float.parseFloat(obj.get("id").toString()),

                        (String) obj.get("type"),
                        (String) obj.get("reclamation"),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateDebut")),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateFin")),
                        makePersonnel((Map<String, Object>) obj.get("personnel")),
                        (String) obj.get("image")

                );

                listPermissions.add(permission);
            }
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
        return listPermissions;
    }


    public Personnel makePersonnel(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }
        try {
            return new Personnel(
                    (int) Float.parseFloat(obj.get("id").toString()),
                    (String) obj.get("nom"),
                    (String) obj.get("prenom"),
                    new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateEmbauche")),
                    (int) Float.parseFloat(obj.get("tel").toString()),
                    (String) obj.get("email"),
                    (String) obj.get("password"),
                    Float.parseFloat(obj.get("salaire").toString()),
                    (int) Float.parseFloat(obj.get("poste").toString()),
                    (int) Float.parseFloat(obj.get("hTravail").toString()),
                    (int) Float.parseFloat(obj.get("hAbsence").toString()),
                    null
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int add(Permission permission) {
        return manage(permission, false, true);
    }

    public int edit(Permission permission, boolean imageEdited) {
        return manage(permission, true, imageEdited);
    }

    public int manage(Permission permission, boolean isEdit, boolean imageEdited) {

        MultipartRequest cr = new MultipartRequest();
        cr.setFilename("file", "Permission.jpg");


        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/permission/edit");
            cr.addArgumentNoEncoding("id", String.valueOf(permission.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/permission/add");
        }

        if (imageEdited) {
            try {
                cr.addData("file", permission.getImage(), "image/jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cr.addArgumentNoEncoding("image", permission.getImage());
        }

        cr.addArgumentNoEncoding("type", permission.getType());
        cr.addArgumentNoEncoding("reclamation", permission.getReclamation());
        cr.addArgumentNoEncoding("dateDebut", new SimpleDateFormat("dd-MM-yyyy").format(permission.getDateDebut()));
        cr.addArgumentNoEncoding("dateFin", new SimpleDateFormat("dd-MM-yyyy").format(permission.getDateFin()));
        cr.addArgumentNoEncoding("personnel", String.valueOf(permission.getPersonnel().getId()));

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultCode = cr.getResponseCode();
                cr.removeResponseListener(this);
            }
        });
        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception ignored) {

        }
        return resultCode;
    }

    public int delete(int permissionId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/permission/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(permissionId));

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                cr.removeResponseListener(this);
            }
        });

        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cr.getResponseCode();
    }
}
