package com.solidgate.config;

import static com.solidgate.config.PropertyReader.PROPERTIES;
import static java.lang.Boolean.parseBoolean;

public final class BrowserParameters {

    private BrowserParameters() {
        throw new UnsupportedOperationException("Utility class. Cannot be instantiated");
    }

    public static boolean isBrowserHeadless() {
        return parseBoolean(PROPERTIES.getProperty("browser.headless"));
    }
}
