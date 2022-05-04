package com.easyFit.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;

import com.easyFit.entities.Salle;
import com.easyFit.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SalleService {

    public static SalleService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Salle> listSalles;

    private SalleService() {
        cr = new ConnectionRequest();
    }

    public static SalleService getInstance() {
        if (instance == null) {
            instance = new SalleService();
        }
        return instance;
    }

    public ArrayList<Salle> getAll() {
        listSalles = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/salle");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listSalles = getList();
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

        return listSalles;
    }

    private ArrayList<Salle> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Salle salle = new Salle(
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

                listSalles.add(salle);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listSalles;
    }

    

    public int add(Salle salle) {
        return manage(salle, false, true);
    }

    public int edit(Salle salle, boolean imageEdited) {
        return manage(salle, true , imageEdited);
    }

    public int manage(Salle salle, boolean isEdit, boolean imageEdited) {
        
        MultipartRequest cr = new MultipartRequest();
        cr.setFilename("file", "Salle.jpg");

        
        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/salle/edit");
            cr.addArgumentNoEncoding("id", String.valueOf(salle.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/salle/add");
        }

        if (imageEdited) {
            try {
                cr.addData("file", salle.getImage(), "image/jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cr.addArgumentNoEncoding("image", salle.getImage());
        }

        cr.addArgumentNoEncoding("nom", salle.getNom());cr.addArgumentNoEncoding("adresse", salle.getAdresse());cr.addArgumentNoEncoding("codePostal", String.valueOf(salle.getCodePostal()));cr.addArgumentNoEncoding("ville", salle.getVille());cr.addArgumentNoEncoding("nombre", String.valueOf(salle.getNombre()));cr.addArgumentNoEncoding("image", salle.getImage());cr.addArgumentNoEncoding("longitude", salle.getLongitude());cr.addArgumentNoEncoding("lattitude", salle.getLattitude());

        
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

    public int delete(int salleId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/salle/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(salleId));

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
