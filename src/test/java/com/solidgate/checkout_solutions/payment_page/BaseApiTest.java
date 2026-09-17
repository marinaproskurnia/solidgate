package com.solidgate.checkout_solutions.payment_page;

import com.solidgate.client.PaymentPageClient;
import com.solidgate.config.TestingConfig;
import com.solidgate.model.request.InitPageRequest;
import com.solidgate.model.request.Order;
import com.solidgate.model.request.PageCustomization;
import com.solidgate.model.response.InitPageResponse;
import com.solidgate.util.JsonBodySerializer;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assumptions;

import java.util.UUID;

import static io.restassured.RestAssured.filters;
import static org.assertj.core.api.Assertions.assertThat;

abstract class BaseApiTest {//here - refactor

    protected static TestingConfig config;
    protected static PaymentPageClient client;

    @BeforeAll
    static void setUpSuite() {
        config = TestingConfig.load();
        client = new PaymentPageClient(config);
        filters(new AllureRestAssured(),
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );
    }

    @BeforeEach
    void requireCredentials() {
        Assumptions.assumeTrue(
                config.hasCredentials(),
                "Configure merchant.public.key and signature.secret.key in testing.properties or env vars"
        );
    }

    protected String generateUniqueOrderId() {
        return UUID.randomUUID().toString();
    }

    protected String toJson(InitPageRequest request) {
        return JsonBodySerializer.toJson(request);
    }

    protected InitPageRequest paymentRequest(String orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setAmount(TestData.PAYMENT_AMOUNT);
        order.setCurrency(TestData.PAYMENT_CURRENCY);
        order.setOrderDescription(TestData.ORDER_DESCRIPTION);
        order.setType(TestData.ORDER_TYPE_AUTH);

        PageCustomization pageCustomization = new PageCustomization(TestData.PUBLIC_NAME);
        pageCustomization.setOrderTitle("Order Title");
        pageCustomization.setOrderDescription(TestData.ORDER_DESCRIPTION);

        return new InitPageRequest(order, pageCustomization);
    }

    protected InitPageRequest subscriptionRequest(String orderId, String productId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setProductId(productId);
        order.setCustomerAccountId(UUID.randomUUID().toString());
        order.setOrderDescription(TestData.ORDER_DESCRIPTION);
        order.setType(TestData.ORDER_TYPE_AUTH);

        return new InitPageRequest(order, new PageCustomization(TestData.PUBLIC_NAME));
    }

    protected InitPageRequest invoiceRequest(String orderId) {
        return invoiceRequest(orderId, TestData.INVOICE_ID);
    }

    protected InitPageRequest invoiceRequest(String orderId, String invoiceId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setInvoiceId(invoiceId);
        order.setOrderDescription(TestData.ORDER_DESCRIPTION);

        return new InitPageRequest(order, new PageCustomization(TestData.PUBLIC_NAME));
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
