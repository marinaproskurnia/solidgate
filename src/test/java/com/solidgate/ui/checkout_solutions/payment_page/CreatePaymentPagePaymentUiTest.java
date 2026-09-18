package com.solidgate.ui.checkout_solutions.payment_page;

import com.solidgate.api.checkout_solutions.payment_page.PaymentPageApiSteps;
import com.solidgate.api.checkout_solutions.payment_page.TestData;
import com.solidgate.model.web.BrowserType;
import com.solidgate.ui.checkout_solutions.payment_page.helpers.CardExpiryGenerator;
import com.solidgate.ui.checkout_solutions.payment_page.pages.PaymentCheckoutPage;
import com.solidgate.ui.checkout_solutions.payment_page.pages.PaymentSuccessPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static com.solidgate.model.web.BrowserType.SAFARI;
import static io.qameta.allure.Allure.step;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class CreatePaymentPagePaymentUiTest extends BaseUiTest {

    @ParameterizedTest
    @EnumSource(BrowserType.class)
    @Tag("TC10")
    @Tag("E2E")
    void shouldCompletePaymentOnHostedPage(BrowserType browserType) {
        step("Check operating system. Safari tests run only on macOS ", () -> {
            checkOperatingSystem(browserType);
        });
        step(format("Start Web browser %s", browserType), () -> {
            try {
                startBrowser(browserType);
            } catch (Exception exception) {
                assumeTrue(false, format("%s is not available: %s", browserType, exception.getMessage()));
            }
        });
        var apiSteps = new PaymentPageApiSteps();
        var createdPageResponse = step("Create Payment page", () -> {
            var orderId = step("Generate unique order ID", apiSteps::generateUniqueOrderId);
            var response = step("Create payment page via API",
                    () -> apiSteps.createPaymentPage(orderId));
            step("Verify payment page API response", () -> {
                assertThat(response.hasError()).isFalse();
                assertThat(response.getUrl()).isNotBlank();
            });
            return response;
        });
        var paymentPageUrl = step("Resolve payment page URL from API response",
                () -> apiSteps.resolvePaymentPageUrl(createdPageResponse));
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
            PaymentSuccessPage successPage = new PaymentSuccessPage(driver)
                    .waitForSuccess(TestData.PAYMENT_SUCCESS_MESSAGE);

            assertThat(successPage.getStatusTitle()).isEqualTo(TestData.PAYMENT_SUCCESS_MESSAGE);
            assertThat(successPage.getOrderDescription()).contains(TestData.ORDER_DESCRIPTION);
            assertThat(successPage.getPriceMajor()).containsIgnoringCase("10.20");
            assertThat(successPage.getOrderTitle()).contains(TestData.ORDER_TITLE);
        });
    }

    private static void checkOperatingSystem(BrowserType browserType) {
        if (browserType == SAFARI) {
            assumeTrue(System.getProperty("os.name").toLowerCase().contains("mac"),
                    "Safari tests run only on macOS");
        }
    }
}
