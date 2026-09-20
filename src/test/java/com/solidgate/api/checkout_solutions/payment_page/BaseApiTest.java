package com.solidgate.api.checkout_solutions.payment_page;

import com.solidgate.api.checkout_solutions.payment_page.steps.PaymentPageRequestFactory;
import com.solidgate.client.PaymentPageClient;
import com.solidgate.config.ApiParameters;
import com.solidgate.model.request.InitPageRequest;
import com.solidgate.model.response.InitPageResponse;
import com.solidgate.util.JsonBodySerializer;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

import static io.restassured.RestAssured.filters;
import static org.assertj.core.api.Assertions.assertThat;

abstract class BaseApiTest {

    protected static PaymentPageClient client;

    @BeforeAll
    static void setUpSuite() {
        client = new PaymentPageClient();
        filters(new AllureRestAssured(),
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );
    }

    @BeforeEach
    void requireCredentials() {
        ApiParameters.requireCredentials();
    }

    protected String generateUniqueOrderId() {
        return UUID.randomUUID().toString();
    }

    protected String toJson(InitPageRequest request) {
        return JsonBodySerializer.toJson(request);
    }

    protected InitPageRequest paymentRequest(String orderId) {
        return PaymentPageRequestFactory.paymentRequest(orderId);
    }

    protected InitPageRequest subscriptionRequest(String orderId, String productId) {
        return PaymentPageRequestFactory.subscriptionRequest(orderId, productId);
    }

    protected InitPageRequest invoiceRequest(String orderId, String invoiceId) {
        return PaymentPageRequestFactory.invoiceRequest(orderId, invoiceId);
    }

    protected void assertSuccessfulPage(InitPageResponse response) {
        assertThat(response.hasError()).isFalse();
        assertThat(response.getUrl()).isNotBlank();
        assertThat(response.getId()).isNotBlank();
        assertThat(response.getUrl()).startsWith("https://payment-page.solidgate.com/");
        assertThat(response.getUrl()).endsWith("/" + response.getId());
        if (response.getGuid() != null) {
            assertThat(response.getGuid()).isNotBlank();
        }
    }
}
