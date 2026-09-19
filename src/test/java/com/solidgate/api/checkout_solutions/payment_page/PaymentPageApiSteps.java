package com.solidgate.api.checkout_solutions.payment_page;

import com.solidgate.client.OrderStatusClient;
import com.solidgate.client.PaymentPageClient;
import com.solidgate.config.ApiParameters;
import com.solidgate.model.response.InitPageResponse;
import com.solidgate.model.response.OrderStatusResponse;
import com.solidgate.util.JsonBodySerializer;

import java.util.UUID;

public final class PaymentPageApiSteps {

    private static final long STATUS_TIMEOUT_MS = 30_000;
    private static final long STATUS_POLL_INTERVAL_MS = 1_000;

    private final PaymentPageClient client = new PaymentPageClient();
    private final OrderStatusClient orderStatusClient = new OrderStatusClient();

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

    /**
     * Polls card-payments {@code POST /status} until {@code order.status} is {@code auth_ok}
     * or the timeout elapses. Returns the latest response so the caller can assert.
     */
    public OrderStatusResponse getOrderStatus(String orderId) {
        long deadline = System.currentTimeMillis() + STATUS_TIMEOUT_MS;
        OrderStatusResponse latest = null;
        while (true) {
            latest = orderStatusClient.getOrderStatus(orderId);
            if (isExpectedStatus(latest) || System.currentTimeMillis() >= deadline) {
                return latest;
            }
            sleepUntilNextPoll();
        }
    }

    private static boolean isExpectedStatus(OrderStatusResponse response) {
        var order = response.getOrder();
        return order != null && TestData.EXPECTED_ORDER_STATUS.equals(order.getStatus());
    }

    private static void sleepUntilNextPoll() {
        try {
            Thread.sleep(STATUS_POLL_INTERVAL_MS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for order status", exception);
        }
    }

    public PaymentPageClient getClient() {
        return client;
    }
}
