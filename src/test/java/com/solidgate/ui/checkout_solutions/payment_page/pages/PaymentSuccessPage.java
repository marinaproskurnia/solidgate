package com.solidgate.ui.checkout_solutions.payment_page.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class PaymentSuccessPage {

    private static final By STATUS_TITLE = By.cssSelector("[data-testid='status-title']");
    private static final By STATUS_ORDER_DESCRIPTION = By.cssSelector("[data-testid='status-order-description']");
    private static final By STATUS_ORDER_TITLE = By.cssSelector("[data-testid='status-order-title']");
    private static final By PRICE_LOCATORS = By.cssSelector(
            "[data-testid='price_major'], [data-testid='price-major'], [data-testid='status-price']"
    );

    private final WebDriver driver;
    private final WebDriverWait wait;

    public PaymentSuccessPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    public PaymentSuccessPage waitForSuccess(String expectedTitle) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(STATUS_TITLE, expectedTitle));
        return this;
    }

    public String getStatusTitle() {
        return getText(STATUS_TITLE);
    }

    public String getOrderDescription() {
        return getText(STATUS_ORDER_DESCRIPTION);
    }

    public String getOrderTitle() {
        return getText(STATUS_ORDER_TITLE);
    }

    public String getPriceMajor() {
        List<WebElement> priceElements = driver.findElements(PRICE_LOCATORS);
        for (WebElement priceElement : priceElements) {
            String text = priceElement.getText().trim();
            if (!text.isEmpty()) {
                return text;
            }
        }
        List<WebElement> nestedPrice = driver.findElements(
                By.xpath("//*[@data-testid='price_major' or @data-testid='price-major']//*")
        );
        for (WebElement pricePart : nestedPrice) {
            String text = pricePart.getText().trim();
            if (!text.isEmpty()) {
                return text;
            }
        }
        return driver.findElement(By.tagName("body")).getText();
    }

    private String getText(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.getText().trim();
    }
}
