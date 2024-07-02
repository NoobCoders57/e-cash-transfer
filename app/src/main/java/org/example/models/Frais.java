package org.example.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Frais(
        String idFrais,
        float montant1,
        float montant2,
        float frais
) {
    public Frais(String idFrais, float montant1, float montant2, float frais) {
        if (montant1 < 0 || montant2 < 0 || frais < 0) {
            throw new IllegalArgumentException("Montant et frais doivent être positifs");
        }
        this.idFrais = idFrais.toLowerCase();
        this.montant1 = montant1;
        this.montant2 = montant2;
        this.frais = frais;
    }

    public String pays() {
        Pattern pattern = Pattern.compile("^[a-zA-Z]*");
        Matcher matcher = pattern.matcher(idFrais);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "";
        }
    }
}
