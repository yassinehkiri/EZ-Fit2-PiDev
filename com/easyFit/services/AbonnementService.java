package com.easyFit.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.easyFit.entities.Abonnement;
import com.easyFit.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AbonnementService {

    public static AbonnementService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Abonnement> listAbonnements;

    private AbonnementService() {
        cr = new ConnectionRequest();
    }

    public static AbonnementService getInstance() {
        if (instance == null) {
            instance = new AbonnementService();
        }
        return instance;
    }

    public ArrayList<Abonnement> getAll() {
        listAbonnements = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/abonnement");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listAbonnements = getList();
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

        return listAbonnements;
    }

    private ArrayList<Abonnement> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Abonnement abonnement = new Abonnement(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("type"),
                        (int) Float.parseFloat(obj.get("tarif").toString()),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateDebut")),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateFin"))

                );

                listAbonnements.add(abonnement);
            }
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
        return listAbonnements;
    }


    public int add(Abonnement abonnement) {
        return manage(abonnement, false);
    }

    public int edit(Abonnement abonnement) {
        return manage(abonnement, true);
    }

    public int manage(Abonnement abonnement, boolean isEdit) {

        cr = new ConnectionRequest();


        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/abonnement/edit");
            cr.addArgument("id", String.valueOf(abonnement.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/abonnement/add");
        }
        cr.addArgument("type", abonnement.getType());
        cr.addArgument("tarif", String.valueOf(abonnement.getTarif()));
        cr.addArgument("dateDebut", new SimpleDateFormat("dd-MM-yyyy").format(abonnement.getDateDebut()));
        cr.addArgument("dateFin", new SimpleDateFormat("dd-MM-yyyy").format(abonnement.getDateFin()));

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

    public int delete(int abonnementId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/abonnement/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(abonnementId));

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
