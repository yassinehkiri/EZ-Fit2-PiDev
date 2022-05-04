package com.easyFit.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.easyFit.entities.Abonne;
import com.easyFit.entities.Abonnement;
import com.easyFit.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AbonneService {

    public static AbonneService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Abonne> listAbonnes;

    private AbonneService() {
        cr = new ConnectionRequest();
    }

    public static AbonneService getInstance() {
        if (instance == null) {
            instance = new AbonneService();
        }
        return instance;
    }

    public ArrayList<Abonne> getAll() {
        listAbonnes = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/abonne");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listAbonnes = getList();
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

        return listAbonnes;
    }

    private ArrayList<Abonne> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Abonne abonne = new Abonne(
                        (int) Float.parseFloat(obj.get("id").toString()),

                        (String) obj.get("nom"),
                        (String) obj.get("prenom"),
                        (int) Float.parseFloat(obj.get("age").toString()),
                        (String) obj.get("sexe"),
                        (String) obj.get("email"),
                        (String) obj.get("mdp"),
                        obj.get("tel").toString(),
                        (String) obj.get("adresse"),
                        makeAbonnement((Map<String, Object>) obj.get("abonnement")),
                        (String) obj.get("message"),
                        (String) obj.get("image")

                );

                listAbonnes.add(abonne);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listAbonnes;
    }


    public Abonnement makeAbonnement(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }

        try {
            return new Abonnement(
                    (int) Float.parseFloat(obj.get("id").toString()),
                    (String) obj.get("type"),
                    (int) Float.parseFloat(obj.get("tarif").toString()),
                    new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateDebut")),
                    new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateFin"))
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int add(Abonne abonne) {
        return manage(abonne, false, true);
    }

    public int edit(Abonne abonne, boolean imageEdited) {
        return manage(abonne, true, imageEdited);
    }

    public int manage(Abonne abonne, boolean isEdit, boolean imageEdited) {

        MultipartRequest cr = new MultipartRequest();
        cr.setFilename("file", "Abonne.jpg");


        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/abonne/edit");
            cr.addArgumentNoEncoding("id", String.valueOf(abonne.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/abonne/add");
        }

        if (imageEdited) {
            try {
                cr.addData("file", abonne.getImage(), "image/jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cr.addArgumentNoEncoding("image", abonne.getImage());
        }

        cr.addArgumentNoEncoding("nom", abonne.getNom());
        cr.addArgumentNoEncoding("prenom", abonne.getPrenom());
        cr.addArgumentNoEncoding("age", String.valueOf(abonne.getAge()));
        cr.addArgumentNoEncoding("sexe", abonne.getSexe());
        cr.addArgumentNoEncoding("email", abonne.getEmail());
        cr.addArgumentNoEncoding("mdp", abonne.getMdp());
        cr.addArgumentNoEncoding("tel", abonne.getTel());
        cr.addArgumentNoEncoding("adresse", abonne.getAdresse());
        cr.addArgumentNoEncoding("abonnement", String.valueOf(abonne.getAbonnement().getId()));
        cr.addArgumentNoEncoding("message", abonne.getMessage());
        cr.addArgumentNoEncoding("image", abonne.getImage());


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

    public int delete(int abonneId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/abonne/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(abonneId));

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
