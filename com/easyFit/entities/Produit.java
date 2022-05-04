package com.easyFit.entities;

public class Produit {

    private int id;
    private String description;
    private String nomP;
    private int nombre;
    private float prix;
    private float reduction;
    private Categorie categorie;
    private String image;

    public Produit(int id, String description, String nomP, int nombre, float prix, float reduction, Categorie categorie, String image) {
        this.id = id;
        this.description = description;
        this.nomP = nomP;
        this.nombre = nombre;
        this.prix = prix;
        this.reduction = reduction;
        this.categorie = categorie;
        this.image = image;
    }

    public Produit(String description, String nomP, int nombre, float prix, float reduction, Categorie categorie, String image) {
        this.description = description;
        this.nomP = nomP;
        this.nombre = nombre;
        this.prix = prix;
        this.reduction = reduction;
        this.categorie = categorie;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNomP() {
        return nomP;
    }

    public void setNomP(String nomP) {
        this.nomP = nomP;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public float getReduction() {
        return reduction;
    }

    public void setReduction(float reduction) {
        this.reduction = reduction;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", nomP='" + nomP + '\'' +
                ", nombre=" + nombre +
                ", prix=" + prix +
                ", reduction=" + reduction +
                ", categorie=" + categorie +
                ", image='" + image + '\'' +
                '}';
    }
}