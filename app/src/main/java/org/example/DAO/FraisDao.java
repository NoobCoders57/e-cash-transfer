package org.example.DAO;

import org.example.models.frais;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FraisDao {
    private Connection connect() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/cash";
        String user = "postgres";
        String password = "mario123";
        return DriverManager.getConnection(url, user, password);
    }

    public List<frais> listAllFrais() throws SQLException {
        List<frais> listFrais = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM FRAIS")) {
            while (rs.next()) {
                int idfrais = rs.getInt("idfrais");
                int montant1 = rs.getInt("montant1");
                int montant2 = rs.getInt("montant2");
                int frais = rs.getInt("frais");
                listFrais.add(new frais(idfrais, montant1, montant2, frais));
            }
        }
        return listFrais;
    }

    public void insertFrais(frais frais) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO FRAIS (montant1, montant2, frais) VALUES (?, ?, ?)")) {
            pstmt.setInt(1, frais.getMontant1());
            pstmt.setInt(2, frais.getMontant2());
            pstmt.setInt(3, frais.getFrais());
            pstmt.executeUpdate();
        }
    }

    public frais getFrais(int idfrais) throws SQLException {
        frais frais = null;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM FRAIS WHERE idfrais = ?")) {
            pstmt.setInt(1, idfrais);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int montant1 = rs.getInt("montant1");
                int montant2 = rs.getInt("montant2");
                int fraisValue = rs.getInt("frais");
                frais = new frais(idfrais, montant1, montant2, fraisValue);
            }
        }
        return frais;
    }

    public void updateFrais(frais frais) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE FRAIS SET montant1 = ?, montant2 = ?, frais = ? WHERE idfrais = ?")) {
            pstmt.setInt(1, frais.getMontant1());
            pstmt.setInt(2, frais.getMontant2());
            pstmt.setInt(3, frais.getFrais());
            pstmt.setInt(4, frais.getIdfrais());
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
