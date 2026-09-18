package com.solidgate.api.checkout_solutions.payment_page;

import com.solidgate.client.PaymentPageClient;
import com.solidgate.config.ApiParameters;
import com.solidgate.model.response.InitPageResponse;
import com.solidgate.util.JsonBodySerializer;

import java.util.UUID;

public final class PaymentPageApiSteps {

    private final PaymentPageClient client = new PaymentPageClient();

    public String generateUniqueOrderId() {
        return UUID.randomUUID().toString();
    }

    public InitPageResponse createPaymentPage(String orderId) {
        String requestBody = JsonBodySerializer.toJson(PaymentPageRequestFactory.paymentRequest(orderId));
        return client.createPaymentPage(requestBody);
    }

    public String buildPaymentPageUrl(String guid) {
        return ApiParameters.getPaymentPageLinkBaseUrl() + "/" + guid;
    }

    /**
     * Resolves the hosted payment page URL from the API response.
     * The API {@code url} field uses {@code id} in the path; {@code guid} is a separate session identifier.
     */
    public String resolvePaymentPageUrl(InitPageResponse response) {
        if (response.getUrl() != null && !response.getUrl().isBlank()) {
            return response.getUrl();
        }
        if (response.getId() != null && !response.getId().isBlank()) {
            return ApiParameters.getPaymentPageLinkBaseUrl() + "/" + response.getId();
        }
        return buildPaymentPageUrl(response.getGuid());
    }

    public PaymentPageClient getClient() {
        return client;
    }
}
