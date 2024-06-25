package org.example.models;

public record Client(
        String numtel,
        String nom,
        String sexe,
        String pays,
        int solde,
        String mail
) {
    public Client {
        if (solde < 0) {
            throw new IllegalArgumentException("Solde doit être positif");
        }
    }
}