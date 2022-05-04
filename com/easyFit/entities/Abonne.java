package com.easyFit.entities;

public class Abonne {

    private int id;
    private String nom;
    private String prenom;
    private int age;
    private String sexe;
    private String email;
    private String mdp;
    private String tel;
    private String adresse;
    private Abonnement abonnement;
    private String message;
    private String image;

    public Abonne(int id, String nom, String prenom, int age, String sexe, String email, String mdp, String tel, String adresse, Abonnement abonnement, String message, String image) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.sexe = sexe;
        this.email = email;
        this.mdp = mdp;
        this.tel = tel;
        this.adresse = adresse;
        this.abonnement = abonnement;
        this.message = message;
        this.image = image;
    }

    public Abonne(String nom, String prenom, int age, String sexe, String email, String mdp, String tel, String adresse, Abonnement abonnement, String message, String image) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.sexe = sexe;
        this.email = email;
        this.mdp = mdp;
        this.tel = tel;
        this.adresse = adresse;
        this.abonnement = abonnement;
        this.message = message;
        this.image = image;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Abonnement getAbonnement() {
        return abonnement;
    }

    public void setAbonnement(Abonnement abonnement) {
        this.abonnement = abonnement;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}