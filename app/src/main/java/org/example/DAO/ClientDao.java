package org.example.DAO;

import org.example.models.client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDao {
    private Connection connect() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/cash";
        String user = "postgres";
        String password = "mario123";
        return DriverManager.getConnection(url, user, password);
    }

    public List<client> listAllClients() throws SQLException {
        List<client> listClients = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM CLIENT")) {
            while (rs.next()) {
                String numtel = rs.getString("numtel");
                String nom = rs.getString("nom");
                String sexe = rs.getString("sexe");
                String pays = rs.getString("pays");
                int solde = rs.getInt("solde");
                String mail = rs.getString("mail");
                listClients.add(new client(numtel, nom, sexe, pays, solde, mail));
            }
        }
        return listClients;
    }

    public void insertClient(client client) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO CLIENT (numtel, nom, sexe, pays, solde, mail) VALUES (?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, client.getNumtel());
            pstmt.setString(2, client.getNom());
            pstmt.setString(3, client.getSexe());
            pstmt.setString(4, client.getPays());
            pstmt.setInt(5, client.getSolde());
            pstmt.setString(6, client.getMail());
            pstmt.executeUpdate();
        }
    }

    public client getClient(String numtel) throws SQLException {
        client client = null;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM CLIENT WHERE numtel = ?")) {
            pstmt.setString(1, numtel);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String nom = rs.getString("nom");
                String sexe = rs.getString("sexe");
                String pays = rs.getString("pays");
                int solde = rs.getInt("solde");
                String mail = rs.getString("mail");
                client = new client(numtel, nom, sexe, pays, solde, mail);
            }
        }
        return client;
    }

    public void updateClient(client client) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE CLIENT SET nom = ?, sexe = ?, pays = ?, solde = ?, mail = ? WHERE numtel = ?")) {
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getSexe());
            pstmt.setString(3, client.getPays());
            pstmt.setInt(4, client.getSolde());
            pstmt.setString(5, client.getMail());
            pstmt.setString(6, client.getNumtel());
            pstmt.executeUpdate();
        }
    }

    public void deleteClient(String numtel) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM CLIENT WHERE numtel = ?")) {
            pstmt.setString(1, numtel);
            pstmt.executeUpdate();
        }
    }
}
