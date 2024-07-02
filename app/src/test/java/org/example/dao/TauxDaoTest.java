package org.example.dao;

import org.example.connection.ConnectionProvider;
import org.example.models.Taux;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TauxDaoTest {
    private TauxDao tauxDao;
    private PreparedStatement preparedStatement;
    private Statement statement;

    @BeforeEach
    void setUp() throws SQLException {
        ConnectionProvider connectionProvider = mock(ConnectionProvider.class);
        Connection connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connectionProvider.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        tauxDao = new TauxDao(connectionProvider);
    }

    @Nested
    class ListAllTaux {
        @Test
        void shouldCallExecuteQuery() throws SQLException {
            tauxDao.listAllTaux();
            verify(statement).executeQuery(anyString());
        }
    }

    @Nested
    class InsertTaux {
        @Test
        void shouldCallExecuteUpdate() throws SQLException {
            Taux taux = new Taux("france", "madagascar", 1, 3000);
            tauxDao.insertTaux(taux);
            verify(preparedStatement).executeUpdate();
        }

        @Test
        void shouldCallTauxMethods() throws SQLException {
            Taux taux = mock(Taux.class);
            when(taux.idTaux()).thenReturn("france-madagascar");
            when(taux.montant1()).thenReturn(1.0f);
            when(taux.montant2()).thenReturn(3000.f);
            tauxDao.insertTaux(taux);
            verify(taux).idTaux();
            verify(taux).montant1();
            verify(taux).montant2();
        }
    }

    @Nested
    class GetTaux {
        @Test
        void shouldCallExecuteQuery() throws SQLException {
            Taux _ = tauxDao.getTaux("france", "madagascar");
            verify(preparedStatement, atLeastOnce()).executeQuery();
        }
    }

    @Nested
    class UpdateTaux {
        @Test
        void shouldCallExecuteUpdate() throws SQLException {
            Taux taux = new Taux("france", "madagascar", 1, 3000);
            tauxDao.updateTaux(taux);
            verify(preparedStatement).executeUpdate();
        }

        @Test
        void shouldCallTauxMethods() throws SQLException {
            Taux taux = mock(Taux.class);
            when(taux.idTaux()).thenReturn("france-madagascar");
            when(taux.montant1()).thenReturn(1.0f);
            when(taux.montant2()).thenReturn(3000f);
            tauxDao.insertTaux(taux);
            verify(taux).idTaux();
            verify(taux).montant1();
            verify(taux).montant2();
        }
    }

    @Nested
    class DeleteTaux {
        @Test
        void shouldCallExecuteUpdate() throws SQLException {
            tauxDao.deleteTaux("france-madagascar");
            verify(preparedStatement).executeUpdate();
        }
    }
}