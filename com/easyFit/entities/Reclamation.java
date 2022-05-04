package com.easyFit.entities;

import java.util.Date;

public class Reclamation {

    private int id;
    private String redacteur;
    private Date date;
    private String contenu;
    private String image;

    public Reclamation(int id, String redacteur, Date date, String contenu, String image) {
        this.id = id;
        this.redacteur = redacteur;
        this.date = date;
        this.contenu = contenu;
        this.image = image;
    }

    public Reclamation(String redacteur, Date date, String contenu, String image) {
        this.redacteur = redacteur;
        this.date = date;
        this.contenu = contenu;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRedacteur() {
        return redacteur;
    }

    public void setRedacteur(String redacteur) {
        this.redacteur = redacteur;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}