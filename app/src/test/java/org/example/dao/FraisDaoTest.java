package org.example.dao;

import org.example.connection.ConnectionProvider;
import org.example.models.Frais;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FraisDaoTest {

    private PreparedStatement preparedStatement;
    private Statement statement;
    private FraisDao fraisDao;

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

        fraisDao = new FraisDao(connectionProvider);

    }

    @Nested
    class ListAllFrais {
        @Test
        void shouldCallExecuteQuery() throws SQLException {
            fraisDao.listAllFrais();
            verify(statement).executeQuery(anyString());
        }
    }

    @Nested
    class InsertFrais {
        @Test
        void shouldCallExecuteUpdate() throws SQLException {
            fraisDao.insertFrais(new Frais(1, 1, 1000, 10));
            verify(preparedStatement).executeUpdate();
        }
    }

    @Nested
    class GetFrais {
        @Test
        void shouldCallExecuteQuery() throws SQLException {
            fraisDao.getFrais(1);
            verify(preparedStatement).executeQuery();
        }
    }

    @Nested
    class DeleteFrais {
        @Test
        void shouldCallExecuteUpdate() throws SQLException {
            fraisDao.deleteFrais(1);
            verify(preparedStatement).executeUpdate();
        }
    }

    @Nested
    class UpdateFrais {
        @Test
        void shouldCallExecuteUpdate() throws SQLException {
            fraisDao.updateFrais(new Frais(1, 1, 1000, 10));
            verify(preparedStatement).executeUpdate();
        }
    }
}