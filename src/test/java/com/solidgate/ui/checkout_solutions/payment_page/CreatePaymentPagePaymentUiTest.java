package com.solidgate.ui.checkout_solutions.payment_page;

import com.solidgate.api.checkout_solutions.payment_page.steps.PaymentPageApiSteps;
import com.solidgate.api.checkout_solutions.payment_page.data.TestData;
import com.solidgate.model.response.InitPageResponse;
import com.solidgate.model.web.BrowserType;
import com.solidgate.ui.checkout_solutions.payment_page.helpers.CardExpiryGenerator;
import com.solidgate.ui.checkout_solutions.payment_page.pages.PaymentCheckoutPage;
import com.solidgate.ui.checkout_solutions.payment_page.pages.PaymentSuccessPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static com.solidgate.api.checkout_solutions.payment_page.data.TestData.EXPECTED_PAYMENT_AMOUNT_DISPLAY;
import static io.qameta.allure.Allure.step;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class CreatePaymentPagePaymentUiTest extends BaseUiTest {

    @ParameterizedTest
    @Tags({@Tag("@TC-UI-1"), @Tag("@e2e")})
    @EnumSource(BrowserType.class)
    void shouldCompletePaymentOnHostedPage(BrowserType browserType) {
        step(format("Start Web browser %s", browserType), () -> {
            try {
                startBrowser(browserType);
            } catch (Exception exception) {
                assumeTrue(false, format("%s is not available: %s", browserType, exception.getMessage()));
            }
        });
        var apiSteps = new PaymentPageApiSteps();
        var createdPage = step("Create Payment page", () -> {
            var orderId = step("Generate unique order ID", apiSteps::generateUniqueOrderId);
            var response = step("Create payment page via API",
                    () -> apiSteps.createPaymentPage(orderId));
            step("Verify payment page API response", () -> {
                assertThat(response.hasError()).isFalse();
                assertThat(response.getUrl()).isNotBlank();
            });
            return new CreatedPaymentPage(orderId, response);
        });
        var paymentPageUrl = step("Resolve payment page URL from API response",
                () -> apiSteps.resolvePaymentPageUrl(createdPage.response()));
        step("Complete payment on hosted page", () -> {
            new PaymentCheckoutPage(driver)
                    .open(paymentPageUrl)
                    .enterCardNumber(TestData.TEST_CARD_NUMBER)
                    .enterCardExpiry(CardExpiryGenerator.generate())
                    .enterCardCvv(TestData.TEST_CARD_CVV)
                    .enterEmail(TestData.TEST_CARD_EMAIL)
                    .submitPayment();
        });
        step("Verify payment success page", () -> {
            var successPage = new PaymentSuccessPage(driver)
                    .waitForSuccess(TestData.PAYMENT_SUCCESS_MESSAGE);

            assertThat(successPage.getStatusTitle()).isEqualTo(TestData.PAYMENT_SUCCESS_MESSAGE);
            assertThat(successPage.getOrderDescription()).contains(TestData.ORDER_DESCRIPTION);
            assertThat(successPage.getPriceMajor()).containsIgnoringCase(EXPECTED_PAYMENT_AMOUNT_DISPLAY);
            assertThat(successPage.getOrderTitle()).contains(TestData.ORDER_TITLE);
        });
        step("Verify order status via POST /status", () -> {
            var status = apiSteps.getOrderStatus(createdPage.orderId());
            assertThat(status.getOrder()).isNotNull();
            assertThat(status.getOrder().getOrderId()).isEqualTo(createdPage.orderId());
            assertThat(status.getOrder().getAmount()).isEqualTo(TestData.PAYMENT_AMOUNT);
            assertThat(status.getOrder().getCurrency()).isEqualTo(TestData.PAYMENT_CURRENCY);
            assertThat(status.getOrder().getStatus()).isEqualTo(TestData.EXPECTED_ORDER_STATUS);
        });
    }

    private record CreatedPaymentPage(String orderId, InitPageResponse response) {
    }
}
