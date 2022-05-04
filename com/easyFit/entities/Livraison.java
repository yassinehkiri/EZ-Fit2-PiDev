package com.easyFit.entities;

import java.util.Date;

public class Livraison {

    private int id;
    private int numL;
    private String nomLivreur;
    private String prenomLivreur;
    private String telLivreur;
    private String adresseLivraison;
    private Date dateDebut;
    private Date dateFin;

    public Livraison(int numL, String nomLivreur, String prenomLivreur, String telLivreur, String adresseLivraison, Date dateDebut, Date dateFin) {
        this.numL = numL;
        this.nomLivreur = nomLivreur;
        this.prenomLivreur = prenomLivreur;
        this.telLivreur = telLivreur;
        this.adresseLivraison = adresseLivraison;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public Livraison(int id, int numL, String nomLivreur, String prenomLivreur, String telLivreur, String adresseLivraison, Date dateDebut, Date dateFin) {
        this.id = id;
        this.numL = numL;
        this.nomLivreur = nomLivreur;
        this.prenomLivreur = prenomLivreur;
        this.telLivreur = telLivreur;
        this.adresseLivraison = adresseLivraison;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumL() {
        return numL;
    }

    public void setNumL(int numL) {
        this.numL = numL;
    }

    public String getNomLivreur() {
        return nomLivreur;
    }

    public void setNomLivreur(String nomLivreur) {
        this.nomLivreur = nomLivreur;
    }

    public String getPrenomLivreur() {
        return prenomLivreur;
    }

    public void setPrenomLivreur(String prenomLivreur) {
        this.prenomLivreur = prenomLivreur;
    }

    public String getTelLivreur() {
        return telLivreur;
    }

    public void setTelLivreur(String telLivreur) {
        this.telLivreur = telLivreur;
    }

    public String getAdresseLivraison() {
        return adresseLivraison;
    }

    public void setAdresseLivraison(String adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }
}
