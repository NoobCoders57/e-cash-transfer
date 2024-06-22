package org.example.models;

import java.util.Date;

public class Envoyer {

    private String numEnvoyeur;
    private String numRecepteur;
    private int montant;
    private Date date;
    private String raison;


    public String getNumEnvoyeur() {
        return numEnvoyeur;
    }

    public void setNumEnvoyeur(String numEnvoyeur) {
        this.numEnvoyeur = numEnvoyeur;
    }

    public String getNumRecepteur() {
        return numRecepteur;
    }

    public void setNumRecepteur(String numRecepteur) {
        this.numRecepteur = numRecepteur;
    }

    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public Envoyer(String numEnvoyeur, String numRecepteur, int montant, Date date, String raison) {
        this.numEnvoyeur = numEnvoyeur;
        this.numRecepteur = numRecepteur;
        this.montant = montant;
        this.date = date;
        this.raison = raison;
    }

}