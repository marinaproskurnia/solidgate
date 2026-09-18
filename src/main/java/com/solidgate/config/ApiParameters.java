package com.solidgate.config;

import static com.solidgate.config.PropertyReader.PROPERTIES;

public final class ApiParameters {

    private ApiParameters() {
        throw new UnsupportedOperationException("Utility class. Cannot be instantiated");
    }

    public static String getMerchantPublicKey() {
        return PROPERTIES.getProperty("merchant.public.key");
    }

    public static String getSignatureSecretKey() {
        return PROPERTIES.getProperty("signature.secret.key");
    }

    public static String getPaymentPageBaseUrl() {
        return PROPERTIES.getProperty("payment.page.base.url");
    }

    public static String getPaymentPageLinkBaseUrl() {
        return PROPERTIES.getProperty("payment.page.link.base.url");
    }

    public static boolean hasCredentials() {
        return isConfigured(getMerchantPublicKey()) && isConfigured(getSignatureSecretKey());
    }

    private static boolean isConfigured(String value) {
        return value != null && !value.isBlank() && !"SET_ME".equals(value);//here
    }
}
