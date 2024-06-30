package org.example.dao;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class AbstractDaoTest {

    @Nested
    class TestConnect {
        @Test
        void testConnect() {
            assertDoesNotThrow(() -> {
                AbstractDao dao = new AbstractDao() {};
                Connection connection = dao.connect();
                assertNotNull(connection);
                connection.close();
            });
        }
    }
}