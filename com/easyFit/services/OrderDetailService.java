package com.easyFit.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.easyFit.entities.OrderDetail;
import com.easyFit.entities.Produit;
import com.easyFit.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDetailService {

    public static OrderDetailService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<OrderDetail> listOrderDetails;

    private OrderDetailService() {
        cr = new ConnectionRequest();
    }

    public static OrderDetailService getInstance() {
        if (instance == null) {
            instance = new OrderDetailService();
        }
        return instance;
    }

    public ArrayList<OrderDetail> getAll() {
        listOrderDetails = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/orderDetail");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listOrderDetails = getList();
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

        return listOrderDetails;
    }

    private ArrayList<OrderDetail> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                OrderDetail orderDetail = new OrderDetail(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (int) Float.parseFloat(obj.get("quantity").toString()),
                        Float.parseFloat(obj.get("prix").toString()),
                        makeProduit((Map<String, Object>) obj.get("produit"))
                );

                listOrderDetails.add(orderDetail);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listOrderDetails;
    }


    public Produit makeProduit(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }

        return new Produit(
                (int) Float.parseFloat(obj.get("id").toString()),
                obj.get("description").toString(),
                (String) obj.get("nomP"),
                (int) Float.parseFloat(obj.get("nombre").toString()),
                Float.parseFloat(obj.get("prix").toString()),
                Float.parseFloat(obj.get("reduction").toString()),
                null,
                (String) obj.get("image")
        );
    }


    public int add(OrderDetail orderDetail) {
        return manage(orderDetail, false);
    }

    public int edit(OrderDetail orderDetail) {
        return manage(orderDetail, true);
    }

    public int manage(OrderDetail orderDetail, boolean isEdit) {

        cr = new ConnectionRequest();


        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/orderDetail/edit");
            cr.addArgument("id", String.valueOf(orderDetail.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/orderDetail/add");
        }
        cr.addArgument("quantity", String.valueOf(orderDetail.getQuantity()));
        cr.addArgument("prix", String.valueOf(orderDetail.getPrix()));
        cr.addArgument("produit", String.valueOf(orderDetail.getProduit().getId()));

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

    public int delete(int orderDetailId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/orderDetail/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(orderDetailId));

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
