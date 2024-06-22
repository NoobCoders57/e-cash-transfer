package org.example.DAO;

import org.example.models.taux;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TauxDao {
    private Connection connect() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/cash";
        String user = "postgres";
        String password = "mario123";
        return DriverManager.getConnection(url, user, password);
    }

    public List<taux> listAllTaux() throws SQLException {
        List<taux> listTaux = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM TAUX")) {
            while (rs.next()) {
                int idtaux = rs.getInt("idtaux");
                int montant1 = rs.getInt("montant1");
                int montant2 = rs.getInt("montant2");
                listTaux.add(new taux(idtaux, montant1, montant2));
            }
        }
        return listTaux;
    }

    public void insertTaux(taux taux) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO TAUX (montant1, montant2) VALUES (?, ?)")) {
            pstmt.setInt(1, taux.getMontant1());
            pstmt.setInt(2, taux.getMontant2());
            pstmt.executeUpdate();
        }
    }

    public taux getTaux(int idtaux) throws SQLException {
        taux taux = null;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM TAUX WHERE idtaux = ?")) {
            pstmt.setInt(1, idtaux);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int montant1 = rs.getInt("montant1");
                int montant2 = rs.getInt("montant2");
                taux = new taux(idtaux, montant1, montant2);
            }
        }
        return taux;
    }

    public void updateTaux(taux taux) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE TAUX SET montant1 = ?, montant2 = ? WHERE idtaux = ?")) {
            pstmt.setInt(1, taux.getMontant1());
            pstmt.setInt(2, taux.getMontant2());
            pstmt.setInt(3, taux.getIdtaux());
            pstmt.executeUpdate();
        }
    }

    public void deleteTaux(int idtaux) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM TAUX WHERE idtaux = ?")) {
            pstmt.setInt(1, idtaux);
            pstmt.executeUpdate();
        }
    }
}
