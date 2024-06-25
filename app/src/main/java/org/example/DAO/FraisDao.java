package org.example.dao;

import org.example.models.Frais;
import org.example.util.Config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FraisDao {
    private Connection connect() throws SQLException {
        String url = Config.get("db.url");
        String user = Config.get("db.user");
        String password = Config.get("db.password");
        return DriverManager.getConnection(url, user, password);
    }

    public List<Frais> listAllFrais() throws SQLException {
        List<Frais> listFrais = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM FRAIS")) {
            while (rs.next()) {
                int idfrais = rs.getInt("idfrais");
                int montant1 = rs.getInt("montant1");
                int montant2 = rs.getInt("montant2");
                int frais = rs.getInt("frais");
                listFrais.add(new Frais(idfrais, montant1, montant2, frais));
            }
        }
        return listFrais;
    }

    public void insertFrais(Frais frais) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO FRAIS (montant1, montant2, frais) VALUES (?, ?, ?)")) {
            pstmt.setInt(1, frais.montant1());
            pstmt.setInt(2, frais.montant2());
            pstmt.setInt(3, frais.frais());
            pstmt.executeUpdate();
        }
    }

    public Frais getFrais(int idfrais) throws SQLException {
        Frais frais = null;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM FRAIS WHERE idfrais = ?")) {
            pstmt.setInt(1, idfrais);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int montant1 = rs.getInt("montant1");
                int montant2 = rs.getInt("montant2");
                int fraisValue = rs.getInt("frais");
                frais = new Frais(idfrais, montant1, montant2, fraisValue);
            }
        }
        return frais;
    }

    public void updateFrais(Frais frais) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE FRAIS SET montant1 = ?, montant2 = ?, frais = ? WHERE idfrais = ?")) {
            pstmt.setInt(1, frais.montant1());
            pstmt.setInt(2, frais.montant2());
            pstmt.setInt(3, frais.frais());
            pstmt.setInt(4, frais.idFrais());
            pstmt.executeUpdate();
        }
    }

    public void deleteFrais(int idfrais) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM FRAIS WHERE idfrais = ?")) {
            pstmt.setInt(1, idfrais);
            pstmt.executeUpdate();
        }
    }
}
