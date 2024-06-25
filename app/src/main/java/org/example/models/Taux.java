package org.example.models;

public record Taux(
        int idTaux,
        int montant1,
        int montant2
) {
    public Taux {
        if (montant1 < 0 || montant2 < 0) {
            throw new IllegalArgumentException("Montant doit être positif");
        }
    }

    public Taux(int montant1, int montant2) {
        this(0, montant1, montant2);
    }
}
