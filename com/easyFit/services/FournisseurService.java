package com.easyFit.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.easyFit.entities.Fournisseur;
import com.easyFit.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FournisseurService {

    public static FournisseurService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Fournisseur> listFournisseurs;

    private FournisseurService() {
        cr = new ConnectionRequest();
    }

    public static FournisseurService getInstance() {
        if (instance == null) {
            instance = new FournisseurService();
        }
        return instance;
    }

    public ArrayList<Fournisseur> getAll() {
        listFournisseurs = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/fournisseur");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listFournisseurs = getList();
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

        return listFournisseurs;
    }

    private ArrayList<Fournisseur> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Fournisseur fournisseur = new Fournisseur(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("nomF"),
                        (String) obj.get("prenomF"),
                        (int) Float.parseFloat(obj.get("telF").toString()),
                        (String) obj.get("emailF"),
                        (String) obj.get("adresse"),
                        (String) obj.get("ribF"),
                        (String) obj.get("image")
                );

                listFournisseurs.add(fournisseur);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listFournisseurs;
    }

    public int add(Fournisseur fournisseur) {
        return manage(fournisseur, false, true);
    }

    public int edit(Fournisseur fournisseur, boolean imageEdited) {
        return manage(fournisseur, true, imageEdited);
    }

    public int manage(Fournisseur fournisseur, boolean isEdit, boolean imageEdited) {
        MultipartRequest cr = new MultipartRequest();
        cr.setHttpMethod("POST");
        cr.setFilename("file", "Fournisseur.jpg");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/fournisseur/edit");
            cr.addArgumentNoEncoding("id", String.valueOf(fournisseur.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/fournisseur/add");
        }

        cr.addArgumentNoEncoding("nomF", fournisseur.getNomF());
        cr.addArgumentNoEncoding("prenomF", fournisseur.getPrenomF());
        cr.addArgumentNoEncoding("telF", String.valueOf(fournisseur.getTelF()));
        cr.addArgumentNoEncoding("emailF", fournisseur.getEmailF());
        cr.addArgumentNoEncoding("adresse", fournisseur.getAdresse());
        cr.addArgumentNoEncoding("ribF", fournisseur.getRibF());

        if (imageEdited) {
            try {
                cr.addData("file", fournisseur.getImage(), "image/jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cr.addArgument("image", fournisseur.getImage());
        }

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

    public int delete(int fournisseurId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/fournisseur/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(fournisseurId));

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
