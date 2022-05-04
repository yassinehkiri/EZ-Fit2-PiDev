package com.easyFit.entities;

import java.util.Date;

public class Abonnement {

    private int id;
    private String type;
    private int tarif;
    private Date dateDebut;
    private Date dateFin;

    public Abonnement(int id, String type, int tarif, Date dateDebut, Date dateFin) {
        this.id = id;
        this.type = type;
        this.tarif = tarif;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public Abonnement(String type, int tarif, Date dateDebut, Date dateFin) {
        this.type = type;
        this.tarif = tarif;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
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

    public int getTarif() {
        return tarif;
    }

    public void setTarif(int tarif) {
        this.tarif = tarif;
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