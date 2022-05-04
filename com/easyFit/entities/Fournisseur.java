package com.easyFit.entities;

public class Fournisseur {

    private int id;
    private String nomF;
    private String prenomF;
    private int telF;
    private String emailF;
    private String adresse;
    private String ribF;
    private String image;

    public Fournisseur(int id, String nomF, String prenomF, int telF, String emailF, String adresse, String ribF, String image) {
        this.id = id;
        this.nomF = nomF;
        this.prenomF = prenomF;
        this.telF = telF;
        this.emailF = emailF;
        this.adresse = adresse;
        this.ribF = ribF;
        this.image = image;
    }

    public Fournisseur(String nomF, String prenomF, int telF, String emailF, String adresse, String ribF, String image) {
        this.nomF = nomF;
        this.prenomF = prenomF;
        this.telF = telF;
        this.emailF = emailF;
        this.adresse = adresse;
        this.ribF = ribF;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomF() {
        return nomF;
    }

    public void setNomF(String nomF) {
        this.nomF = nomF;
    }

    public String getPrenomF() {
        return prenomF;
    }

    public void setPrenomF(String prenomF) {
        this.prenomF = prenomF;
    }

    public int getTelF() {
        return telF;
    }

    public void setTelF(int telF) {
        this.telF = telF;
    }

    public String getEmailF() {
        return emailF;
    }

    public void setEmailF(String emailF) {
        this.emailF = emailF;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getRibF() {
        return ribF;
    }

    public void setRibF(String ribF) {
        this.ribF = ribF;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
