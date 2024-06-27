package org.example.dao;

import org.example.models.Taux;
import org.example.util.Config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TauxDao {
    private Connection connect() throws SQLException {
        String url = Config.get("db.url");
        String user = Config.get("db.user");
        String password = Config.get("db.password");
        return DriverManager.getConnection(url, user, password);
    }

    public List<Taux> listAllTaux() throws SQLException {
        List<Taux> listTaux = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM TAUX")) {
            while (rs.next()) {
                int idtaux = rs.getInt("idtaux");
                int montant1 = rs.getInt("montant1");
                int montant2 = rs.getInt("montant2");
                listTaux.add(new Taux(idtaux, montant1, montant2));
            }
        }
        return listTaux;
    }

    public void insertTaux(Taux taux) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO TAUX (montant1, montant2) VALUES (?, ?)")) {
            pstmt.setInt(1, taux.montant1());
            pstmt.setInt(2, taux.montant2());
            pstmt.executeUpdate();
        }
    }

    public Taux getTaux(int idtaux) throws SQLException {
        Taux taux = null;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM TAUX WHERE idtaux = ?")) {
            pstmt.setInt(1, idtaux);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int montant1 = rs.getInt("montant1");
                int montant2 = rs.getInt("montant2");
                taux = new Taux(idtaux, montant1, montant2);
            }
        }
        return taux;
    }

    public void updateTaux(Taux taux) throws SQLException {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("UPDATE TAUX SET montant1 = ?, montant2 = ? WHERE idtaux = ?")) {
            pstmt.setInt(1, taux.montant1());
            pstmt.setInt(2, taux.montant2());
            pstmt.setInt(3, taux.idTaux());
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
