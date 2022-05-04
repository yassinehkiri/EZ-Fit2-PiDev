package com.easyFit.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.easyFit.entities.Equipement;
import com.easyFit.entities.Fournisseur;
import com.easyFit.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EquipementService {

    public static EquipementService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Equipement> listEquipements;

    private EquipementService() {
        cr = new ConnectionRequest();
    }

    public static EquipementService getInstance() {
        if (instance == null) {
            instance = new EquipementService();
        }
        return instance;
    }

    public ArrayList<Equipement> getAll() {
        listEquipements = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/equipement");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listEquipements = getList();
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

        return listEquipements;
    }

    private ArrayList<Equipement> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Equipement equipement = new Equipement(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("nomE"),
                        (String) obj.get("typeE"),
                        (String) obj.get("marque"),
                        (String) obj.get("gamme"),
                        (int) Float.parseFloat(obj.get("quantite").toString()),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateCommande")),
                        Float.parseFloat(obj.get("prix").toString()),
                        makeFournisseur((Map<String, Object>) obj.get("fournisseur"))
                );

                listEquipements.add(equipement);
            }
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
        return listEquipements;
    }

    public Fournisseur makeFournisseur(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }


        return new Fournisseur(
                (int) Float.parseFloat(obj.get("id").toString()),
                (String) obj.get("nomF"),
                (String) obj.get("prenomF"),
                (int) Float.parseFloat(obj.get("telF").toString()),
                (String) obj.get("emailF"),
                (String) obj.get("adresse"),
                (String) obj.get("ribF"),
                (String) obj.get("image")
        );
    }

    public int add(Equipement equipement) {
        return manage(equipement, false);
    }

    public int edit(Equipement equipement) {
        return manage(equipement, true);
    }

    public int manage(Equipement equipement, boolean isEdit) {
        cr = new ConnectionRequest();
        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/equipement/edit");
            cr.addArgument("id", String.valueOf(equipement.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/equipement/add");
        }

        cr.addArgument("nomE", equipement.getNomE());
        cr.addArgument("typeE", equipement.getTypeE());
        cr.addArgument("marque", equipement.getMarque());
        cr.addArgument("gamme", equipement.getGamme());
        cr.addArgument("quantite", String.valueOf(equipement.getQuantite()));
        cr.addArgument("dateCommande", new SimpleDateFormat("dd-MM-yyyy").format(equipement.getDateCommande()));
        cr.addArgument("prix", String.valueOf(equipement.getPrix()));
        cr.addArgumentNoEncoding("fournisseur", String.valueOf(equipement.getFournisseur().getId()));

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

    public int delete(int equipementId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/equipement/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(equipementId));

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
