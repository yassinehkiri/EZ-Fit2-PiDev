package com.easyFit.entities;

import java.util.ArrayList;

public class User {

    private int id;
    private String email;
    private String username;
    private String password;
    private String confirm_password;
    private ArrayList<String> roles;

    public User(int id, String email, String username, String password, String confirm_password, ArrayList<String> roles) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirm_password = confirm_password;
        this.roles = roles;
    }

    public User(String email, String username, String password, String confirm_password, ArrayList<String> roles) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirm_password = confirm_password;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }

    public String toutInfo() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", confirm_password='" + confirm_password + '\'' +
                ", roles=" + roles +
                '}';
    }

    @Override
    public String toString() {
        return email;
    }
}