package com.easyFit.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.easyFit.entities.Categorie;
import com.easyFit.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategorieService {

    public static CategorieService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Categorie> listCategories;

    private CategorieService() {
        cr = new ConnectionRequest();
    }

    public static CategorieService getInstance() {
        if (instance == null) {
            instance = new CategorieService();
        }
        return instance;
    }

    public ArrayList<Categorie> getAll() {
        listCategories = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/categorie");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listCategories = getList();
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

        return listCategories;
    }

    private ArrayList<Categorie> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Categorie categorie = new Categorie(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("nomC"),
                        (String) obj.get("type")
                );

                listCategories.add(categorie);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listCategories;
    }

    public int add(Categorie categorie) {
        return manage(categorie, false);
    }

    public int edit(Categorie categorie) {
        return manage(categorie, true);
    }

    public int manage(Categorie categorie, boolean isEdit) {
        cr = new ConnectionRequest();
        cr.setHttpMethod("POST");

        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/categorie/edit");
            cr.addArgument("id", String.valueOf(categorie.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/categorie/add");
        }

        cr.addArgument("nomC", categorie.getNomC());
        cr.addArgument("type", categorie.getType());

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

    public int delete(int categorieId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/categorie/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(categorieId));

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
