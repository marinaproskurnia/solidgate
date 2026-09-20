package com.solidgate.ui.checkout_solutions.payment_page.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.solidgate.config.ApiParameters.getWebDriverWaitTimeoutSec;

public class PaymentCheckoutPage {

    private static final By CARD_NUMBER = By.cssSelector("[data-testid='cardNumber']");
    private static final By CARD_EXPIRY = By.cssSelector("[data-testid='cardExpiryDate']");
    private static final By CARD_CVV = By.cssSelector("[data-testid='cardCvv']");
    private static final By EMAIL = By.cssSelector("[data-testid='charity-email']");
    private static final By SUBMIT = By.cssSelector("[data-testid='submit']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public PaymentCheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(getWebDriverWaitTimeoutSec()));
    }

    public PaymentCheckoutPage open(String paymentPageUrl) {
        driver.get(paymentPageUrl);
        wait.until(ExpectedConditions.visibilityOfElementLocated(CARD_NUMBER));
        return this;
    }

    public PaymentCheckoutPage enterCardNumber(String cardNumber) {
        fillField(CARD_NUMBER, cardNumber);
        return this;
    }

    public PaymentCheckoutPage enterCardExpiry(String expiry) {
        fillField(CARD_EXPIRY, expiry);
        return this;
    }

    public PaymentCheckoutPage enterCardCvv(String cvv) {
        fillField(CARD_CVV, cvv);
        return this;
    }

    public PaymentCheckoutPage enterEmail(String email) {
        fillField(EMAIL, email);
        return this;
    }

    public PaymentCheckoutPage submitPayment() {
        WebElement submitButton = wait.until(ExpectedConditions.presenceOfElementLocated(SUBMIT));
        wait.until(driver -> {
            WebElement button = driver.findElement(SUBMIT);
            String cssClass = button.getAttribute("class");
            return button.isEnabled() && (cssClass == null || !cssClass.contains("disabled"));
        });
        wait.until(ExpectedConditions.elementToBeClickable(SUBMIT)).click();
        return this;
    }

    private void fillField(By locator, String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        field.click();
        field.clear();
        field.sendKeys(value);
        field.sendKeys(Keys.TAB);
    }
}
