package org.example.models;

public class taux {
    private int montant1;
    private int montant2;

    public int getMontant2() {
        return montant2;
    }

    public void setMontant2(int montant2) {
        this.montant2 = montant2;
    }

    public int getMontant1() {
        return montant1;
    }

    public void setMontant1(int montant1) {
        this.montant1 = montant1;
    }



    public taux(int montant1, int montant2) {
        this.montant1 = montant1;
        this.montant2 = montant2;
    }

}
