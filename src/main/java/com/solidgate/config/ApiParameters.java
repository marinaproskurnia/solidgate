package com.solidgate.config;

import static com.solidgate.config.PropertyReader.GLOBAL_PROPERTIES;
import static java.lang.Long.parseLong;

public final class ApiParameters {

    private ApiParameters() {
        throw new UnsupportedOperationException("Utility class. Cannot be instantiated");
    }

    public static String getMerchantPublicKey() {
        return GLOBAL_PROPERTIES.get("merchant.public.key");
    }

    public static String getSignatureSecretKey() {
        return GLOBAL_PROPERTIES.get("signature.secret.key");
    }

    public static String getPaymentPageBaseUrl() {
        return GLOBAL_PROPERTIES.get("payment.page.base.url");
    }

    public static String getPaymentPageLinkBaseUrl() {
        return GLOBAL_PROPERTIES.get("payment.page.link.base.url");
    }

    public static String getCardPaymentsBaseUrl() {
        return GLOBAL_PROPERTIES.get("card.payments.base.url");
    }

    public static long getWebDriverWaitTimeoutSec() {
        return parseLong(GLOBAL_PROPERTIES.get("web.driver.wait.timeout.sec"));
    }

    public static long getOrderStatusTimeoutSec() {
        return parseLong(GLOBAL_PROPERTIES.get("order.status.timeout.sec"));
    }

    public static long getOrderStatusPollIntervalMs() {
        return parseLong(GLOBAL_PROPERTIES.get("order.status.poll.interval.ms"));
    }

    public static boolean hasCredentials() {
        return isConfigured(getMerchantPublicKey()) && isConfigured(getSignatureSecretKey());
    }

    private static boolean isConfigured(String value) {
        return value != null && !value.isBlank() && !"SET_ME".equals(value);
    }
}
