package org.example.dao;

import org.example.connection.ConnectionProvider;
import org.example.connection.HikariConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDao {
    protected ConnectionProvider connectionProvider;

    protected AbstractDao() {
        this(new HikariConnectionPool());
    }

    protected AbstractDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    protected Connection connect() throws SQLException {
        return connectionProvider.getConnection();
    }
}
