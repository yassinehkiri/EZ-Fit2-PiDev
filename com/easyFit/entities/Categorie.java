package com.easyFit.entities;

public class Categorie {

    private int id;
    private String nomC;
    private String type;
    private Produit[] produits;

    public Categorie(int id, String nomC, String type) {
        this.id = id;
        this.nomC = nomC;
        this.type = type;
    }

    public Categorie(String nomC, String type) {
        this.nomC = nomC;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomC() {
        return nomC;
    }

    public void setNomC(String nomC) {
        this.nomC = nomC;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
