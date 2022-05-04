package com.easyFit.entities;

import java.util.Date;

public class Permission {

    private int id;
    private String type;
    private String reclamation;
    private Date dateDebut;
    private Date dateFin;
    private Personnel personnel;
    private String image;

    public Permission(int id, String type, String reclamation, Date dateDebut, Date dateFin, Personnel personnel, String image) {
        this.id = id;
        this.type = type;
        this.reclamation = reclamation;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.personnel = personnel;
        this.image = image;
    }

    public Permission(String type, String reclamation, Date dateDebut, Date dateFin, Personnel personnel, String image) {
        this.type = type;
        this.reclamation = reclamation;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.personnel = personnel;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReclamation() {
        return reclamation;
    }

    public void setReclamation(String reclamation) {
        this.reclamation = reclamation;
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

    public Personnel getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}