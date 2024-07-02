package org.example.dao;

import org.example.connection.ConnectionProvider;
import org.example.models.Frais;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FraisDao extends AbstractDao {
    public FraisDao(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    public FraisDao() {
        super();
    }

    public List<Frais> listAllFrais() throws SQLException {
        List<Frais> listFrais = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM FRAIS")) {
            while (rs.next()) {
                String idfrais = rs.getString("idfrais");
                float montant1 = rs.getFloat("montant1");
                float montant2 = rs.getFloat("montant2");
                float frais = rs.getFloat("frais");
                listFrais.add(new Frais(idfrais, montant1, montant2, frais));
            }
        }
        return listFrais;
    }

    public void insertFrais(@NotNull Frais frais) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO FRAIS (idfrais, montant1, montant2, frais) VALUES ((? || nextval('frais_idfrais_seq'::regclass)), ?, ?, ?)")
        ) {
            pstmt.setString(1, frais.idFrais());
            pstmt.setFloat(2, frais.montant1());
            pstmt.setFloat(3, frais.montant2());
            pstmt.setFloat(4, frais.frais());
            pstmt.executeUpdate();
        }
    }

    @Nullable
    public Frais getFrais(String idfrais) throws SQLException {
        Frais frais = null;
        idfrais = idfrais.toLowerCase();
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM FRAIS WHERE idfrais = ?")) {
            pstmt.setString(1, idfrais);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                float montant1 = rs.getFloat("montant1");
                float montant2 = rs.getFloat("montant2");
                float fraisValue = rs.getFloat("frais");
                frais = new Frais(idfrais, montant1, montant2, fraisValue);
            }
        }
        return frais;
    }

    @Nullable
    public Frais getFraisForMontant(float montant, String pays) throws SQLException {
        Frais frais = null;
        pays = pays.toLowerCase();
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM FRAIS WHERE idfrais LIKE ? AND montant1 <= ? AND montant2 >= ?")) {
            pstmt.setString(1, "%" + pays + "%");
            pstmt.setFloat(2, montant);
            pstmt.setFloat(3, montant);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String idfrais = rs.getString("idfrais");
                float montant1 = rs.getFloat("montant1");
                float montant2 = rs.getFloat("montant2");
                float fraisValue = rs.getFloat("frais");
                frais = new Frais(idfrais, montant1, montant2, fraisValue);
            }
        }
        return frais;
    }

    public float getFraisValueForMontant(float montant, String pays) throws SQLException {
        Frais frais = getFraisForMontant(montant, pays);
        return frais != null ? frais.frais() : 0;
    }

    public void updateFrais(@NotNull Frais frais) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE FRAIS SET montant1 = ?, montant2 = ?, frais = ? WHERE idfrais = ?")) {
            pstmt.setFloat(1, frais.montant1());
            pstmt.setFloat(2, frais.montant2());
            pstmt.setFloat(3, frais.frais());
            pstmt.setString(4, frais.idFrais());
            pstmt.executeUpdate();
        }
    }

    public void deleteFrais(String idfrais) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM FRAIS WHERE idfrais = ?")) {
            pstmt.setString(1, idfrais);
            pstmt.executeUpdate();
        }
    }
}
