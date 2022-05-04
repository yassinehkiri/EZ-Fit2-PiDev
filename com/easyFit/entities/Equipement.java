package com.easyFit.entities;

import java.util.Date;

public class Equipement {

    private int id;
    private String nomE;
    private String typeE;
    private String marque;
    private String gamme;
    private int quantite;
    private Date dateCommande;
    private float prix;
    private Fournisseur fournisseur;

    public Equipement(int id, String nomE, String typeE, String marque, String gamme, int quantite, Date dateCommande, float prix, Fournisseur fournisseur) {
        this.id = id;
        this.nomE = nomE;
        this.typeE = typeE;
        this.marque = marque;
        this.gamme = gamme;
        this.quantite = quantite;
        this.dateCommande = dateCommande;
        this.prix = prix;
        this.fournisseur = fournisseur;
    }

    public Equipement(String nomE, String typeE, String marque, String gamme, int quantite, Date dateCommande, float prix, Fournisseur fournisseur) {
        this.nomE = nomE;
        this.typeE = typeE;
        this.marque = marque;
        this.gamme = gamme;
        this.quantite = quantite;
        this.dateCommande = dateCommande;
        this.prix = prix;
        this.fournisseur = fournisseur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomE() {
        return nomE;
    }

    public void setNomE(String nomE) {
        this.nomE = nomE;
    }

    public String getTypeE() {
        return typeE;
    }

    public void setTypeE(String typeE) {
        this.typeE = typeE;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getGamme() {
        return gamme;
    }

    public void setGamme(String gamme) {
        this.gamme = gamme;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }
}
