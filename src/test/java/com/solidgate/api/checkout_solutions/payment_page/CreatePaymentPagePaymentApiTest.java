package com.solidgate.api.checkout_solutions.payment_page;

import com.solidgate.api.checkout_solutions.payment_page.data.TestData;
import com.solidgate.config.ApiParameters;
import com.solidgate.model.request.InitPageRequest;
import com.solidgate.model.request.Order;
import com.solidgate.model.request.PageCustomization;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.solidgate.api.checkout_solutions.payment_page.data.TestData.ORDER_DESCRIPTION;
import static com.solidgate.api.checkout_solutions.payment_page.data.TestData.PAYMENT_CURRENCY;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

class CreatePaymentPagePaymentApiTest extends BaseApiTest {

    @Test
    @Tag("TC1")
    void shouldCreatePaymentPageForOrder() {
        var orderId = step("Generate unique order ID", this::generateUniqueOrderId);
        var requestBody = step("Build payment page request body",
                () -> toJson(paymentRequest(orderId)));
        var response = step("Create payment page",
                () -> client.createPaymentPage(requestBody)
        );
        step("Verify payment page response is successful", () -> assertSuccessfulPage(response));
    }

    @Test
    @Tag("TC4")
    void shouldFailWithInvalidSignature() {
        var requestBody = step("Build payment page request body",
                () -> toJson(paymentRequest(generateUniqueOrderId())));
        var response = step("Create payment page with invalid signature",
                () -> client.createPaymentPage(requestBody, ApiParameters.getMerchantPublicKey(), "invalid-signature"));
        step("Verify authentication error response", () -> {
            assertThat(response.hasError()).isTrue();
            assertThat(response.getError().getCode()).isEqualTo("1.01");
            assertThat(response.getError().getMessageAsText()).containsIgnoringCase("authentication failed");
        });
    }

    @Test
    @Tag("TC5")
    void shouldFailWhenInvalidAmountValue() {
        var requestBody = step("Build payment page request body without amount", () -> {
            var order = new Order();
            order.setOrderId(generateUniqueOrderId());
            order.setAmount(-1);
            order.setCurrency(PAYMENT_CURRENCY);
            order.setOrderDescription(ORDER_DESCRIPTION);

            InitPageRequest request = new InitPageRequest(order, new PageCustomization(TestData.PUBLIC_NAME));
            return toJson(request);
        });
        var response = step("Create payment page", () -> client.createPaymentPage(requestBody));
        step("Verify error response", () -> {
            assertThat(response.hasError()).isTrue();
            assertThat(response.getError().getCode()).isEqualTo("2.01");
            assertThat(response.getError().getMessage().isValidation()).isTrue();
            assertThat(response.getError().getMessage().getValidation().getOrderFieldError("amount"))
                    .isEqualTo("must be greater than or equal to 0");
        });
    }

    @Test
    @Tag("TC6")
    void shouldFailWhenRequiredCurrencyMissing() {
        var requestBody = step("Build payment page request body without currency", () -> {
            var order = new Order();
            order.setOrderId(generateUniqueOrderId());
            order.setOrderDescription(ORDER_DESCRIPTION);

            var request = new InitPageRequest(order, new PageCustomization(TestData.PUBLIC_NAME));
            return toJson(request);
        });
        var response = step("Create payment page", () -> client.createPaymentPage(requestBody));
        step("Verify error response", () -> {
            assertThat(response.hasError()).isTrue();
            assertThat(response.getError().getCode()).isEqualTo("2.01");
            assertThat(response.getError().getMessage().isValidation()).isTrue();
            assertThat(response.getError().getMessage().getValidation().getOrderFieldError("currency"))
                    .isEqualTo("cannot be blank");
        });
    }

    @Test
    @Tag("TC7")
    void shouldFailWhenPublicNameMissing() {
        var requestBody = step("Build payment page request body without public name", () -> {
            Order order = new Order();
            order.setOrderId(generateUniqueOrderId());
            order.setAmount(TestData.PAYMENT_AMOUNT);
            order.setCurrency(PAYMENT_CURRENCY);
            order.setOrderDescription(ORDER_DESCRIPTION);

            InitPageRequest request = new InitPageRequest(order, new PageCustomization(null));
            return toJson(request);
        });
        var response = step("Create payment page", () -> client.createPaymentPage(requestBody));
        step("Verify error response", () -> {
            assertThat(response.hasError()).isTrue();
            assertThat(response.getError().getCode()).isEqualTo("2.01");
            assertThat(response.getError().getMessage().isValidation()).isTrue();
            assertThat(response.getError().getMessage().getValidation().getPageCustomizationFieldError("public_name"))
                    .isEqualTo("cannot be blank");
        });
    }

    @Test
    @Tag("TC8")
    void shouldFailWhenInvoiceIdDoesNotExist() {
        var orderId = step("Generate unique order ID", this::generateUniqueOrderId);
        var requestBody = step("Build invoice payment page request body with non-existing invoice ID",
                () -> toJson(invoiceRequest(orderId, "inv_nonexisting_value")));
        var response = step("Create payment page", () -> client.createPaymentPage(requestBody));
        step("Verify error response", () -> {
            assertThat(response.hasError()).isTrue();
            assertThat(response.getError().getCode()).isEqualTo("2.01");
            assertThat(response.getError().getMessage().isList()).isTrue();
            assertThat(response.getError().getMessage().getList()).contains("Invoice not found");
        });
    }

    @Test
    @Tag("TC9")
    void shouldFailWhenSubscriptionIdDoesNotExist() {
        var orderId = step("Generate unique order ID", this::generateUniqueOrderId);
        var requestBody = step("Build subscription payment page request body with non-existing product ID",
                () -> toJson(subscriptionRequest(orderId, UUID.randomUUID().toString())));
        var response = step("Create payment page", () -> client.createPaymentPage(requestBody));
        step("Verify error response", () -> {
            assertThat(response.hasError()).isTrue();
            assertThat(response.getError().getCode()).isEqualTo("2.01");
            assertThat(response.getError().getMessage().isList()).isTrue();
            assertThat(response.getError().getMessage().getList()).contains("Subscription error");
        });
    }
}
