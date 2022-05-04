package com.easyFit.entities;

public class OrderDetail {

    private int id;
    private int quantity;
    private float prix;
    private Produit produit;

    public OrderDetail(int id, int quantity, float prix, Produit produit) {
        this.id = id;
        this.quantity = quantity;
        this.prix = prix;
        this.produit = produit;
    }

    public OrderDetail(int quantity, float prix, Produit produit) {
        this.quantity = quantity;
        this.prix = prix;
        this.produit = produit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", prix=" + prix +
                ", produit=" + produit +
                '}';
    }
}