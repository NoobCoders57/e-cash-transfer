package org.example.dao;

import org.example.connection.ConnectionProvider;
import org.example.models.Envoyer;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class EnvoyerDao extends AbstractDao {
    public enum EnvoyerType {
        SENT,
        RECEIVED,
        ALL
    }

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
                listEnvoyer.add(getEnvoyerFromResultSet(rs));
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
                envoyer = getEnvoyerFromResultSet(rs);
            }
        }
        return envoyer;
    }

    private static @NotNull Envoyer getEnvoyerFromResultSet(@NotNull ResultSet rs) throws SQLException {
        String numEnvoyeur = rs.getString("numEnvoyeur");
        String numRecepteur = rs.getString("numRecepteur");
        int montant = rs.getInt("montant");
        Date date = rs.getTimestamp("date");
        String raison = rs.getString("raison");
        int idEnv = rs.getInt("idEnv");
        return new Envoyer(idEnv, numEnvoyeur, numRecepteur, montant, date, raison);
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

    /**
     * Get all transactions of a client based on the type of transactions
     * @param clientNumTel the client's phone number
     * @param start the start date
     * @param end the end date
     * @param type the type of transactions
     * @return a list of transactions
     * @throws SQLException if a database access error occurs
     * @see Envoyer
     */
    public List<Envoyer> allTransactions(String clientNumTel, Date start, Date end, @NotNull EnvoyerType type) throws SQLException {
        List<Envoyer> listEnvoyer = new ArrayList<>();
        String query = "SELECT * FROM ENVOYER WHERE (date BETWEEN ? AND ?) ";

        query += switch (type) {
            case SENT -> "AND numEnvoyeur = ?";
            case RECEIVED -> "AND numRecepteur = ?";
            case ALL -> "AND (numEnvoyeur = ? OR numRecepteur = ?)";
        };

        try (
                Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(query)
        ) {
            pstmt.setTimestamp(1, new Timestamp(start.getTime()));
            pstmt.setTimestamp(2, new Timestamp(end.getTime()));

            switch (type) {
                case SENT, RECEIVED -> pstmt.setString(3, clientNumTel);
                case ALL -> {
                    pstmt.setString(3, clientNumTel);
                    pstmt.setString(4, clientNumTel);
                }
                default -> throw new IllegalStateException("Unexpected value: " + type);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                listEnvoyer.add(getEnvoyerFromResultSet(rs));
            }
        }
        return listEnvoyer;
    }

    /**
     * Get all transactions where the client is the cash receiver
     * @param clientNumTel the client's phone number
     * @param start the start date
     * @param end the end date
     * @return a list of transactions
     * @throws SQLException if a database access error occurs
     * @see Envoyer
     */
    public List<Envoyer> receivedTransactions(String clientNumTel, Date start, Date end) throws SQLException {
        return allTransactions(clientNumTel, start, end, EnvoyerType.RECEIVED);
    }

    /**
     * Get all transactions where the client is the cash sender
     * @param clientNumTel the client's phone number
     * @param start the start date
     * @param end the end date
     * @return a list of transactions
     * @throws SQLException if a database access error occurs
     * @see Envoyer
     */
    public List<Envoyer> sentTransactions(String clientNumTel, Date start, Date end) throws SQLException {
        return allTransactions(clientNumTel, start, end, EnvoyerType.SENT);
    }

    /**
     * Get all transactions of a client, whether they are the sender or the receiver
     * @param clientNumTel the client's phone number
     * @param start the start date
     * @param end the end date
     * @return a list of transactions
     * @throws SQLException if a database access error occurs
     * @see Envoyer
     */
    public List<Envoyer> allTransactions(String clientNumTel, Date start, Date end) throws SQLException {
        return allTransactions(clientNumTel, start, end, EnvoyerType.ALL);
    }
}
