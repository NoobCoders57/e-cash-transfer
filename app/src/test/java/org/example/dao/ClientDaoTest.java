package org.example.dao;

import org.example.connection.ConnectionProvider;
import org.example.models.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ClientDaoTest {

    private ClientDao clientDao;
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

        clientDao = new ClientDao(connectionProvider);
    }

    @Nested
    class ListAllClients {
        @Test
        void shouldCallExecuteQuery() throws SQLException {
            clientDao.listAllClients();
            verify(statement, times(1)).executeQuery(anyString());
        }
    }

    @Nested
    class InsertClient {
        @Test
        void shouldCallExecuteUpdate() throws SQLException {
            clientDao.insertClient(new Client("1234567890", "Test", "M", "USA", 1000, "test@example.com"));
            verify(preparedStatement, times(1)).executeUpdate();
        }
    }

    @Nested
    class GetClient {
        @Test
        void shouldCallExecuteQuery() throws SQLException {
            clientDao.getClient("1234567890");
            verify(preparedStatement, times(1)).executeQuery();
        }
    }

    @Nested
    class UpdateClient {
        @Test
        void shouldCallExecuteUpdate() throws SQLException {
            clientDao.updateClient(new Client("1234567890", "Test", "M", "USA", 1000, "testupdate@example.com"));
            verify(preparedStatement, times(1)).executeUpdate();
        }
    }

    @Nested
    class DeleteClient {
        @Test
        void shouldCallExecuteUpdate() throws SQLException {
            clientDao.deleteClient("1234567890");
            verify(preparedStatement, times(1)).executeUpdate();
        }
    }
}