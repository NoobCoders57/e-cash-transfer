package org.example.util.config;

import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Could not load config.properties file", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
