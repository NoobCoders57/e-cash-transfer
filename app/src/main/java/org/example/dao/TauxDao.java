package org.example.dao;

import org.example.connection.ConnectionProvider;
import org.example.models.Taux;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TauxDao extends AbstractDao {
    public TauxDao() {
        super();
    }

    public TauxDao(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    public List<Taux> listAllTaux() throws SQLException {
        List<Taux> listTaux = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM TAUX")) {
            while (rs.next()) {
                String[] pays = rs.getString("idtaux").split("-");
                float montant1 = rs.getFloat("montant1");
                float montant2 = rs.getFloat("montant2");
                listTaux.add(new Taux(pays[0], pays[1], montant1, montant2));
            }
        }
        return listTaux;
    }

    public void insertTaux(Taux taux) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO TAUX (idtaux, montant1, montant2) VALUES (?, ?, ?)")) {
            pstmt.setString(1, taux.idTaux());
            pstmt.setFloat(2, taux.montant1());
            pstmt.setFloat(3, taux.montant2());
            pstmt.executeUpdate();
        }
    }

    public Taux getTaux(String pays1, String pays2) throws SQLException {
        Taux taux = null;
        pays1 = pays1.toLowerCase();
        pays2 = pays2.toLowerCase();
        String idtaux = pays1 + "-" + pays2;
        String altIdTaux = pays2 + "-" + pays1;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM TAUX WHERE idtaux = ?")) {
            pstmt.setString(1, idtaux);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                float montant1 = rs.getFloat("montant1");
                float montant2 = rs.getFloat("montant2");
                taux = new Taux(pays1, pays2, montant1, montant2);
            }
        }
        if (taux == null) {
            try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM TAUX WHERE idtaux = ?")) {
                pstmt.setString(1, altIdTaux);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    float montant1 = rs.getFloat("montant1");
                    float montant2 = rs.getFloat("montant2");
                    taux = new Taux(pays1, pays2, montant2, montant1);
                }
            }
        }
        return taux;
    }

    public void updateTaux(@NotNull Taux taux) throws SQLException {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("UPDATE TAUX SET montant1 = ?, montant2 = ? WHERE idtaux = ?")) {
            pstmt.setFloat(1, taux.montant1());
            pstmt.setFloat(2, taux.montant2());
            pstmt.setString(3, taux.idTaux());
            pstmt.executeUpdate();
        }
    }

    public void deleteTaux(String idtaux) throws SQLException {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM TAUX WHERE idtaux = ?")) {
            pstmt.setString(1, idtaux);
            pstmt.executeUpdate();
        }
    }

    /**
     * Convert a montant from one devise to another
     *
     * @param paysFrom devise de départ
     * @param paysTo   devise d'arrivée
     * @param montant  montant à convertir
     * @return montant converti
     */
    public float convert(String paysFrom, String paysTo, float montant) throws SQLException {
        Taux taux = getTaux(paysFrom, paysTo);
        return taux != null? (montant * taux.montant2()) / taux.montant1(): montant;
    }
}
