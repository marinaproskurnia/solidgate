package com.solidgate.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertyReader {

    static final Properties PROPERTIES = loadProperties();

    private PropertyReader() {
        throw new UnsupportedOperationException("Utility class. Cannot be instantiated");
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = PropertyReader.class.getClassLoader()
                .getResourceAsStream("testing.properties")) {
            if (inputStream == null) {
                throw new IllegalStateException("testing.properties not found on classpath");
            }
            properties.load(inputStream);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load testing.properties", exception);
        }
        return properties;
    }
}
