package com.solidgate.config;

import static com.solidgate.config.PropertyReader.GLOBAL_PROPERTIES;

public final class BrowserParameters {

    private BrowserParameters() {
        throw new UnsupportedOperationException("Utility class. Cannot be instantiated");
    }

    public static boolean isBrowserHeadless() {
        return GLOBAL_PROPERTIES.getBoolean("browser.headless", false);
    }
}
