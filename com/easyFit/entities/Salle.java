package com.easyFit.entities;

import java.util.Date;

public class Salle {
    
    private int id;
    private String nom;
    private String adresse;
    private int codePostal;
    private String ville;
    private int nombre;
    private String image;
    private String longitude;
    private String lattitude;
    
    public Salle(int id, String nom, String adresse, int codePostal, String ville, int nombre, String image, String longitude, String lattitude) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.ville = ville;
        this.nombre = nombre;
        this.image = image;
        this.longitude = longitude;
        this.lattitude = lattitude;
    }

    public Salle(String nom, String adresse, int codePostal, String ville, int nombre, String image, String longitude, String lattitude) {
        this.nom = nom;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.ville = ville;
        this.nombre = nombre;
        this.image = image;
        this.longitude = longitude;
        this.lattitude = lattitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }
    
    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }
    
    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }
    
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    
    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }
    
}