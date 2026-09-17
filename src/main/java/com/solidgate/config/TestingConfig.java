package com.solidgate.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

public final class TestingConfig {

    private static final String LOCAL_PROPERTIES = "testing.local.properties";
    private static final String DEFAULT_PROPERTIES = "testing.properties";

    private final String merchantPublicKey;
    private final String signatureSecretKey;
    private final String paymentPageBaseUrl;

    private TestingConfig(String merchantPublicKey, String signatureSecretKey, String paymentPageBaseUrl) {
        this.merchantPublicKey = merchantPublicKey;
        this.signatureSecretKey = signatureSecretKey;
        this.paymentPageBaseUrl = paymentPageBaseUrl;
    }

    public static TestingConfig load() {
        Properties properties = new Properties();
        loadFromClasspath(properties, DEFAULT_PROPERTIES);
        loadFromClasspath(properties, LOCAL_PROPERTIES);
        loadFromFileSystem(properties, Path.of("src/test/resources", LOCAL_PROPERTIES));

        String merchantPublicKey = resolve("merchant.public.key", "MERCHANT_PUBLIC_KEY", properties);
        String signatureSecretKey = resolve("signature.secret.key", "SIGNATURE_SECRET_KEY", properties);
        String paymentPageBaseUrl = resolve(
                "payment.page.base.url",
                "PAYMENT_PAGE_BASE_URL",
                properties,
                "https://payment-page.solidgate.com/api/v1"
        );

        return new TestingConfig(merchantPublicKey, signatureSecretKey, paymentPageBaseUrl);
    }

    public String getMerchantPublicKey() {
        return merchantPublicKey;
    }

    public String getSignatureSecretKey() {
        return signatureSecretKey;
    }

    public String getPaymentPageBaseUrl() {
        return paymentPageBaseUrl;
    }

    public boolean hasCredentials() {
        return isConfigured(merchantPublicKey) && isConfigured(signatureSecretKey);
    }

    private static String resolve(String propertyKey, String envKey, Properties properties) {
        return resolve(propertyKey, envKey, properties, null);
    }

    private static String resolve(String propertyKey, String envKey, Properties properties, String defaultValue) {
        return Optional.ofNullable(System.getenv(envKey))
                .filter(TestingConfig::isConfigured)
                .or(() -> Optional.ofNullable(properties.getProperty(propertyKey)).filter(TestingConfig::isConfigured))
                .orElse(defaultValue);
    }

    private static boolean isConfigured(String value) {
        return value != null && !value.isBlank() && !"CHANGE_ME".equals(value);
    }

    private static void loadFromClasspath(Properties properties, String fileName) {
        try (InputStream inputStream = TestingConfig.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load " + fileName, exception);
        }
    }

    private static void loadFromFileSystem(Properties properties, Path path) {
        if (!Files.exists(path)) {
            return;
        }
        try (InputStream inputStream = Files.newInputStream(path)) {
            properties.load(inputStream);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load " + path, exception);
        }
    }
}
