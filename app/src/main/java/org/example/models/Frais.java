package org.example.models;

public record Frais(
        int idFrais,
        int montant1,
        int montant2,
        int frais
) {
    public Frais {
        if (montant1 < 0 || montant2 < 0 || frais < 0) {
            throw new IllegalArgumentException("Montant et frais doivent être positifs");
        }
    }

    public Frais(int montant1, int montant2, int frais) {
        this(0, montant1, montant2, frais);
    }
}
