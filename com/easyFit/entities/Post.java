package com.easyFit.entities;

import java.util.ArrayList;
import java.util.Date;

public class Post {

    private int id;
    private String object;
    private String content;
    private Date date;
    private String image;
    private ArrayList<Comment> comments;
    private int userId;

    public Post(int id, String object, String content, Date date, String image, ArrayList<Comment> comments, int userId) {
        this.id = id;
        this.object = object;
        this.content = content;
        this.date = date;
        this.image = image;
        this.comments = comments;
        this.userId = userId;
    }

    public Post(String object, String content, String image, int userId) {
        this.object = object;
        this.content = content;
        this.image = image;
        this.userId = userId;
    }

    public Post(int id, String object, String content, String image) {
        this.id = id;
        this.object = object;
        this.content = content;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", object='" + object + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", image='" + image + '\'' +
                ", comments=" + comments +
                ", userId=" + userId +
                '}';
    }
}