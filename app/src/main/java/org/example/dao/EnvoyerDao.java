package org.example.dao;

import org.example.connection.ConnectionProvider;
import org.example.models.Envoyer;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnvoyerDao extends AbstractDao {
    public EnvoyerDao(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    public EnvoyerDao() {
        super();
    }

    public List<Envoyer> listAllEnvois() throws SQLException {
        List<Envoyer> listEnvoyer = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM ENVOYER")) {

            while (rs.next()) {
                int idEnv = rs.getInt("idEnv");
                String numEnvoyeur = rs.getString("numEnvoyeur");
                String numRecepteur = rs.getString("numRecepteur");
                int montant = rs.getInt("montant");
                Date date = rs.getDate("date");
                String raison = rs.getString("raison");
                listEnvoyer.add(new Envoyer(idEnv, numEnvoyeur, numRecepteur, montant, date, raison));
            }
        }
        return listEnvoyer;
    }

    public void insertEnvoyer(@NotNull Envoyer envoyer) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ENVOYER (numEnvoyeur, numRecepteur, montant, date, raison) VALUES (?, ?, ?, ?, ?)")) {
            pstmt.setString(1, envoyer.numEnvoyeur());
            pstmt.setString(2, envoyer.numRecepteur());
            pstmt.setInt(3, envoyer.montant());
            pstmt.setTimestamp(4, new Timestamp(envoyer.date().getTime()));
            pstmt.setString(5, envoyer.raison());
            pstmt.executeUpdate();
        }
    }

    public Envoyer getEnvoyer(int idEnv) throws SQLException {
        Envoyer envoyer = null;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ENVOYER WHERE idEnv = ?")) {
            pstmt.setInt(1, idEnv);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String numEnvoyeur = rs.getString("numEnvoyeur");
                String numRecepteur = rs.getString("numRecepteur");
                int montant = rs.getInt("montant");
                Date date = rs.getDate("date");
                String raison = rs.getString("raison");
                envoyer = new Envoyer(idEnv, numEnvoyeur, numRecepteur, montant, date, raison);
            }
        }
        return envoyer;
    }

    public void updateEnvoyer(@NotNull Envoyer envoyer) throws SQLException {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("UPDATE ENVOYER SET numEnvoyeur = ?, numRecepteur = ?, montant = ?, raison = ? WHERE idEnv = ?")) {
            pstmt.setString(1, envoyer.numEnvoyeur());
            pstmt.setString(2, envoyer.numRecepteur());
            pstmt.setInt(3, envoyer.montant());
            pstmt.setString(4, envoyer.raison());
            pstmt.setInt(5, envoyer.idEnv());
            pstmt.executeUpdate();
        }
    }

    public void deleteEnvoyer(int idEnv) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ENVOYER WHERE idEnv = ?")) {
            pstmt.setInt(1, idEnv);
            pstmt.executeUpdate();
        }
    }
}
