package org.example.models;

import java.util.Date;

public record Envoyer(
        int idEnv,
        String numEnvoyeur,
        String numRecepteur,
        float montant,
        Date date,
        String raison,
        float frais
) {
    public Envoyer {
        if (montant < 0) {
            throw new IllegalArgumentException("Montant doit être positif");
        }
        if (frais < 0) {
            throw new IllegalArgumentException("Frais doit être positif");
        }
    }

    public Envoyer(String numEnvoyeur, String numRecepteur, float montant, Date date, String raison) {
        this(0, numEnvoyeur, numRecepteur, montant, date, raison, 0.0f);
    }
}