package org.example.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class HikariConnectionPoolTest {

    @Nested
    class TestGetConnection {
        @Test
        void testGetConnection() {
            assertDoesNotThrow(() -> {
                ConnectionProvider connectionProvider = new HikariConnectionPool();
                Connection connection = connectionProvider.getConnection();
                assertNotNull(connection);
                assertFalse(connection.isClosed());
                connection.close();
                assertTrue(connection.isClosed());
            });
        }

        @Test
        void testGetConnectionWithException() {
            assertThrows(HikariPool.PoolInitializationException.class, () -> {
                HikariConfig config = new HikariConfig();
                config.setJdbcUrl("jdbc:postgresql://localhost:5432/nonexistent");
                config.setUsername("postgres");
                config.setPassword("postgres");
                ConnectionProvider connectionProvider = new HikariConnectionPool(config);
                Connection connection = connectionProvider.getConnection();
                connection.close();
            });
        }
    }
}