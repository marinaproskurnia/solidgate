package com.solidgate.config;

public final class PropertyReader {

    static final GlobalProperties GLOBAL_PROPERTIES = GlobalProperties.getInstance();

    private PropertyReader() {
        throw new UnsupportedOperationException("Utility class. Cannot be instantiated");
    }
}
