package org.example.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {
    @Test
    void testPropertiesLoad() {
        assertDoesNotThrow(() -> Config.get("db.url"));
    }

    @Test
    void testRequiredProperties() {
        assertAll(
                () -> assertNotNull(Config.get("db.url")),
                () -> assertNotNull(Config.get("db.user")),
                () -> assertNotNull(Config.get("db.password")),
                () -> assertNotNull(Config.get("mail.username")),
                () -> assertNotNull(Config.get("mail.password")),
                () -> assertNotNull(Config.get("mail.from"))
        );
    }
}