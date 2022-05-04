package com.easyFit.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.easyFit.entities.Personnel;
import com.easyFit.entities.Salle;
import com.easyFit.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonnelService {

    public static PersonnelService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Personnel> listPersonnels;

    private PersonnelService() {
        cr = new ConnectionRequest();
    }

    public static PersonnelService getInstance() {
        if (instance == null) {
            instance = new PersonnelService();
        }
        return instance;
    }

    public ArrayList<Personnel> getAll() {
        listPersonnels = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/personnel");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listPersonnels = getList();
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

        return listPersonnels;
    }

    private ArrayList<Personnel> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Personnel personnel = new Personnel(
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
                        makeSalle((Map<String, Object>) obj.get("salle"))

                );

                listPersonnels.add(personnel);
            }
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
        return listPersonnels;
    }


    public Salle makeSalle(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }

        return new Salle(
                (int) Float.parseFloat(obj.get("id").toString()),

                (String) obj.get("nom"),
                (String) obj.get("adresse"),
                (int) Float.parseFloat(obj.get("codePostal").toString()),
                (String) obj.get("ville"),
                (int) Float.parseFloat(obj.get("nombre").toString()),
                (String) obj.get("image"),
                (String) obj.get("longitude"),
                (String) obj.get("lattitude")

        );
    }


    public int add(Personnel personnel) {
        return manage(personnel, false);
    }

    public int edit(Personnel personnel) {
        return manage(personnel, true);
    }

    public int manage(Personnel personnel, boolean isEdit) {

        cr = new ConnectionRequest();


        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/personnel/edit");
            cr.addArgument("id", String.valueOf(personnel.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/personnel/add");
        }
        cr.addArgument("nom", personnel.getNom());
        cr.addArgument("prenom", personnel.getPrenom());
        cr.addArgument("dateEmbauche", new SimpleDateFormat("dd-MM-yyyy").format(personnel.getDateEmbauche()));
        cr.addArgument("tel", String.valueOf(personnel.getTel()));
        cr.addArgument("email", personnel.getEmail());
        cr.addArgument("password", personnel.getPassword());
        cr.addArgument("salaire", String.valueOf(personnel.getSalaire()));
        cr.addArgument("poste", String.valueOf(personnel.getPoste()));
        cr.addArgument("hTravail", String.valueOf(personnel.getHTravail()));
        cr.addArgument("hAbsence", String.valueOf(personnel.getHAbsence()));
        cr.addArgument("salle", String.valueOf(personnel.getSalle().getId()));

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

    public int delete(int personnelId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/personnel/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(personnelId));

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
