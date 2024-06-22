package org.example.models;

public class client {
    private String numtel;
    private String nom;
    private String sexe;
    private String pays;
    private int solde;
    private String mail;

    public String getNumtel() {
        return numtel;
    }

    public void setNumtel(String numtel) {
        this.numtel = numtel;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public int getSolde() {
        return solde;
    }

    public void setSolde(int solde) {
        this.solde = solde;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }


    public client(String numtel, String nom, String sexe, String pays, int solde, String mail) {
        this.numtel = numtel;
        this.nom = nom;
        this.sexe = sexe;
        this.pays = pays;
        this.solde = solde;
        this.mail = mail;
    }

    // Getters and Setters
}