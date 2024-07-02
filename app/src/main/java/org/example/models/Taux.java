package org.example.models;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Taux(
        String pays1,
        String pays2,
        float montant1,
        float montant2
) {
    public Taux(@NotNull String pays1, @NotNull String pays2, float montant1, float montant2) {
        this.pays1 = pays1.toLowerCase();
        this.pays2 = pays2.toLowerCase();
        if (montant1 < 0 || montant2 < 0) {
            throw new IllegalArgumentException("Montant doit être positif");
        }
        this.montant1 = montant1;
        this.montant2 = montant2;
    }

    @Contract(pure = true)
    public @NotNull String idTaux() {
        return pays1 + "-" + pays2;
    }
}
