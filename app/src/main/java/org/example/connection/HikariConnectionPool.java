package org.example.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.util.config.Config;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariConnectionPool implements ConnectionProvider {
    private static final HikariConfig CONFIG;

    private static final HikariDataSource DATA_SOURCE;

    static {
        CONFIG = new HikariConfig();
        CONFIG.setJdbcUrl(Config.get("db.url"));
        CONFIG.setUsername(Config.get("db.user"));
        CONFIG.setPassword(Config.get("db.password"));
        CONFIG.setMaximumPoolSize(Integer.parseInt(Config.get("db.pool.size")));

        DATA_SOURCE = new HikariDataSource(CONFIG);
    }

    public HikariConnectionPool() { }

    @Override
    public Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}
