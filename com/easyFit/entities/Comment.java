package com.easyFit.entities;

import java.util.Date;

public class Comment {

    private int id;
    private String content;
    private Date date;
    private Post post;
    private int userId;

    public Comment(int id, String content, Date date, Post post, int userId) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.post = post;
        this.userId = userId;
    }

    public Comment(int id, String content, Post post) {
        this.id = id;
        this.content = content;
        this.post = post;
    }

    public Comment(String content, Post post, int userId) {
        this.content = content;
        this.post = post;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", post=" + post +
                ", userId=" + userId +
                '}';
    }
}