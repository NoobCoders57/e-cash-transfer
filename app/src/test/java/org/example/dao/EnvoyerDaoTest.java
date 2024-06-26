package org.example.dao;

import org.example.connection.ConnectionProvider;
import org.example.models.Envoyer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Date;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EnvoyerDaoTest {

    private PreparedStatement preparedStatement;
    private Statement statement;
    private EnvoyerDao clientDao;

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

        clientDao = new EnvoyerDao(connectionProvider);
    }

    @Nested
    class ListAllEnvois {
        @Test
        void shouldCallExecuteQuery() throws SQLException {
            clientDao.listAllEnvois();
            verify(statement, times(1)).executeQuery(anyString());
        }
    }

    @Nested
    class InsertEnvoyer {
        @Test
        void shouldCallExecuteUpdate() throws SQLException {
            clientDao.insertEnvoyer(new Envoyer(1, "1234567890", "0987654321", 1000, new Date(), "Test"));
            verify(preparedStatement, times(1)).executeUpdate();
        }
    }

    @Nested
    class GetEnvoyer {
        @Test
        void shouldCallExecuteQuery() throws SQLException {
            clientDao.getEnvoyer(1);
            verify(preparedStatement, times(1)).executeQuery();
        }
    }

    @Nested
    class DeleteEnvoyer {
        @Test
        void shouldCallExecuteUpdate() throws SQLException {
            clientDao.deleteEnvoyer(1);
            verify(preparedStatement, times(1)).executeUpdate();
        }
    }

    @Nested
    class UpdateEnvoyer {
        @Test
        void shouldCallExecuteUpdate() throws SQLException {
            clientDao.updateEnvoyer(new Envoyer(1, "1234567890", "0987654321", 1000, new Date(), "Test"));
            verify(preparedStatement, times(1)).executeUpdate();
        }
    }
}