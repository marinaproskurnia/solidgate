package com.solidgate.api.checkout_solutions.payment_page.steps;

import com.solidgate.api.checkout_solutions.payment_page.data.TestData;
import com.solidgate.client.OrderStatusClient;
import com.solidgate.client.PaymentPageClient;
import com.solidgate.config.ApiParameters;
import com.solidgate.model.response.InitPageResponse;
import com.solidgate.model.response.OrderStatusResponse;
import com.solidgate.util.JsonBodySerializer;

import org.awaitility.core.ConditionTimeoutException;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;

public final class PaymentPageApiSteps {

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

    public String resolvePaymentPageUrl(InitPageResponse response) {
        if (response.getUrl() != null && !response.getUrl().isBlank()) {
            return response.getUrl();
        }
        if (response.getId() != null && !response.getId().isBlank()) {
            return ApiParameters.getPaymentPageLinkBaseUrl() + "/" + response.getId();
        }
        return buildPaymentPageUrl(response.getGuid());
    }

    public OrderStatusResponse getOrderStatus(String orderId) {
        AtomicReference<OrderStatusResponse> latest = new AtomicReference<>();
        try {
            await()
                    .atMost(Duration.ofSeconds(ApiParameters.getOrderStatusTimeoutSec()))
                    .pollInterval(Duration.ofMillis(ApiParameters.getOrderStatusPollIntervalMs()))
                    .until(() -> {
                        OrderStatusResponse response = orderStatusClient.getOrderStatus(orderId);
                        latest.set(response);
                        return isExpectedStatus(response);
                    });
        } catch (ConditionTimeoutException exception) {
            throw new IllegalStateException(
                    "Timed out waiting for order status. orderId=%s, expected=%s, actual=%s"
                            .formatted(orderId, TestData.EXPECTED_ORDER_STATUS, resolveStatus(latest.get())),
                    exception);
        }
        return latest.get();
    }

    private static boolean isExpectedStatus(OrderStatusResponse response) {
        var order = response.getOrder();
        return order != null && TestData.EXPECTED_ORDER_STATUS.equals(order.getStatus());
    }

    private static String resolveStatus(OrderStatusResponse response) {
        if (response == null || response.getOrder() == null) {
            return "<no order>";
        }
        var status = response.getOrder().getStatus();
        return status != null ? status : "<null>";
    }

    public PaymentPageClient getClient() {
        return client;
    }
}
