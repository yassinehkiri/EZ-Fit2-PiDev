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

public class PostService {

    public static PostService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Post> listPosts;

    private PostService() {
        cr = new ConnectionRequest();
    }

    public static PostService getInstance() {
        if (instance == null) {
            instance = new PostService();
        }
        return instance;
    }

    public ArrayList<Post> getAll() {
        listPosts = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/post");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listPosts = getList();
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

        return listPosts;
    }

    private ArrayList<Post> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Post post = new Post(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("object"),
                        (String) obj.get("content"),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("date")),
                        (String) obj.get("image"),
                        makeComments((List<Map<String, Object>>) obj.get("comments")),
                        (int) Float.parseFloat(obj.get("userId").toString())
                );

                listPosts.add(post);
            }
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
        return listPosts;
    }
        // Add Comment (Metier)
    public ArrayList<Comment> makeComments(List<Map<String, Object>> list) {
        ArrayList<Comment> listComments = new ArrayList<>();

        if (list == null) {
            return null;
        }

        try {
            for (Map<String, Object> obj : list) {
                Comment comment = new Comment(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("content"),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("date")),
                        null,
                        (int) Float.parseFloat(obj.get("userId").toString())
                );

                listComments.add(comment);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return listComments;
    }
     // CRUD
    public int add(Post post) {
        return manage(post, false, true);
    }

    public int edit(Post post, boolean imageEdited) {
        return manage(post, true, imageEdited);
    }

    public int manage(Post post, boolean isEdit, boolean imageEdited) {
        MultipartRequest cr = new MultipartRequest();
        cr.setHttpMethod("POST");
        cr.setFilename("file", "Post.jpg");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/post/edit");
            cr.addArgumentNoEncoding("id", String.valueOf(post.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/post/add");
        }

        cr.addArgumentNoEncoding("object", post.getObject());
        cr.addArgumentNoEncoding("content", post.getContent());
        cr.addArgumentNoEncoding("userId", String.valueOf(post.getUserId()));

        if (imageEdited) {
            try {
                cr.addData("file", post.getImage(), "image/jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cr.addArgument("image", post.getImage());
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

    public int delete(int postId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/post/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(postId));

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
