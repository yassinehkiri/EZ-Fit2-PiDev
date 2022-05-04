package com.easyFit.entities;

import java.util.Date;

public class Personnel {

    private int id;
    private String nom;
    private String prenom;
    private Date dateEmbauche;
    private int tel;
    private String email;
    private String password;
    private float salaire;
    private int poste;
    private int hTravail;
    private int hAbsence;
    private Salle salle;

    public Personnel(int id, String nom, String prenom, Date dateEmbauche, int tel, String email, String password, float salaire, int poste, int hTravail, int hAbsence, Salle salle) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateEmbauche = dateEmbauche;
        this.tel = tel;
        this.email = email;
        this.password = password;
        this.salaire = salaire;
        this.poste = poste;
        this.hTravail = hTravail;
        this.hAbsence = hAbsence;
        this.salle = salle;
    }

    public Personnel(String nom, String prenom, Date dateEmbauche, int tel, String email, String password, float salaire, int poste, int hTravail, int hAbsence, Salle salle) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateEmbauche = dateEmbauche;
        this.tel = tel;
        this.email = email;
        this.password = password;
        this.salaire = salaire;
        this.poste = poste;
        this.hTravail = hTravail;
        this.hAbsence = hAbsence;
        this.salle = salle;
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(Date dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getSalaire() {
        return salaire;
    }

    public void setSalaire(float salaire) {
        this.salaire = salaire;
    }

    public int getPoste() {
        return poste;
    }

    public void setPoste(int poste) {
        this.poste = poste;
    }

    public int getHTravail() {
        return hTravail;
    }

    public void setHTravail(int hTravail) {
        this.hTravail = hTravail;
    }

    public int getHAbsence() {
        return hAbsence;
    }

    public void setHAbsence(int hAbsence) {
        this.hAbsence = hAbsence;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

}