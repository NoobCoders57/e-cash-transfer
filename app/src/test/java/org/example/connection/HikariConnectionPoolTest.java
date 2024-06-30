package org.example.connection;

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
    }
}