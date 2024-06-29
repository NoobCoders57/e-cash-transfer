package org.example.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.util.config.Config;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariConnectionPool implements ConnectionProvider {
    private static final HikariConfig CONFIG;

    private final HikariDataSource ds;

    static {
        CONFIG = new HikariConfig();
        CONFIG.setJdbcUrl(Config.get("db.url"));
        CONFIG.setUsername(Config.get("db.user"));
        CONFIG.setPassword(Config.get("db.password"));
        CONFIG.setMaximumPoolSize(10);
    }

    public HikariConnectionPool() {
        this(CONFIG);
    }

    public HikariConnectionPool(HikariConfig configuration) {
        this.ds = new HikariDataSource(configuration);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
