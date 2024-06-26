package org.example.dao;

import org.example.connection.ConnectionProvider;
import org.example.models.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDao extends AbstractDao {
    public ClientDao(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    public ClientDao() {
        super();
    }

    public List<Client> listAllClients() throws SQLException {
        List<Client> listClients = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM CLIENT")
        ) {
            while (rs.next()) {
                String numtel = rs.getString("numtel");
                String nom = rs.getString("nom");
                String sexe = rs.getString("sexe");
                String pays = rs.getString("pays");
                int solde = rs.getInt("solde");
                String mail = rs.getString("mail");
                listClients.add(new Client(numtel, nom, sexe, pays, solde, mail));
            }
        }
        return listClients;
    }

    public void insertClient(Client client) throws SQLException {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO CLIENT (numtel, nom, sexe, pays, solde, mail) VALUES (?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, client.numtel());
            pstmt.setString(2, client.nom());
            pstmt.setString(3, client.sexe());
            pstmt.setString(4, client.pays());
            pstmt.setInt(5, client.solde());
            pstmt.setString(6, client.mail());
            pstmt.executeUpdate();
        }
    }

    public Client getClient(String numtel) throws SQLException {
        Client client = null;
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
                client = new Client(numtel, nom, sexe, pays, solde, mail);
            }
        }
        return client;
    }

    public void updateClient(Client client) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE CLIENT SET nom = ?, sexe = ?, pays = ?, solde = ?, mail = ? WHERE numtel = ?")) {
            pstmt.setString(1, client.nom());
            pstmt.setString(2, client.sexe());
            pstmt.setString(3, client.pays());
            pstmt.setInt(4, client.solde());
            pstmt.setString(5, client.mail());
            pstmt.setString(6, client.numtel());
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
