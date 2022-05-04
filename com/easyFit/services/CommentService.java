package com.easyFit.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.easyFit.entities.Comment;
import com.easyFit.entities.Post;
import com.easyFit.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommentService {

    public static CommentService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Comment> listComments;

    private CommentService() {
        cr = new ConnectionRequest();
    }

    public static CommentService getInstance() {
        if (instance == null) {
            instance = new CommentService();
        }
        return instance;
    }

    public ArrayList<Comment> getAll() {
        listComments = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/comment");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listComments = getList();
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

        return listComments;
    }

    private ArrayList<Comment> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Comment comment = new Comment(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("content"),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("date")),
                        makePost((Map<String, Object>) obj.get("post")),
                        (int) Float.parseFloat(obj.get("userId").toString())
                );

                listComments.add(comment);
            }
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
        return listComments;
    }

    public Post makePost(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }

        try {
            return new Post(
                    (int) Float.parseFloat(obj.get("id").toString()),
                    (String) obj.get("object"),
                    (String) obj.get("content"),
                    new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("date")),
                    (String) obj.get("image"),
                    null,
                    (int) Float.parseFloat(obj.get("userId").toString())
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int add(Comment comment) {
        return manage(comment, false);
    }

    public int edit(Comment comment) {
        return manage(comment, true);
    }

    public int manage(Comment comment, boolean isEdit) {
        cr = new ConnectionRequest();
        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/comment/edit");
            cr.addArgument("id", String.valueOf(comment.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/comment/add");
        }

        cr.addArgument("content", comment.getContent());
        cr.addArgument("post", String.valueOf(comment.getPost().getId()));
        cr.addArgumentNoEncoding("userId", String.valueOf(comment.getUserId()));

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

    public int delete(int commentId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/comment/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(commentId));

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
