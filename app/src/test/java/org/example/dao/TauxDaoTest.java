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
            Taux taux = new Taux(1, 1, 3000);
            tauxDao.insertTaux(taux);
            verify(preparedStatement).executeUpdate();
        }
    }

    @Nested
    class GetTaux {
        @Test
        void shouldCallExecuteQuery() throws SQLException {
            tauxDao.getTaux(1);
            verify(preparedStatement).executeQuery();
        }
    }

    @Nested
    class UpdateTaux {
        @Test
        void shouldCallExecuteUpdate() throws SQLException {
            Taux taux = new Taux(1, 1, 3000);
            tauxDao.updateTaux(taux);
            verify(preparedStatement).executeUpdate();
        }
    }

    @Nested
    class DeleteTaux {
        @Test
        void shouldCallExecuteUpdate() throws SQLException {
            tauxDao.deleteTaux(1);
            verify(preparedStatement).executeUpdate();
        }
    }
}