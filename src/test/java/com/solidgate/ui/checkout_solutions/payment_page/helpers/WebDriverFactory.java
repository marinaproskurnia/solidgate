package com.solidgate.ui.checkout_solutions.payment_page.helpers;

import com.solidgate.config.BrowserParameters;
import com.solidgate.model.web.BrowserType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public final class WebDriverFactory {

    private WebDriverFactory() {}

    public static WebDriver create(BrowserType browserType) {
        return switch (browserType) {
            case CHROME -> createChromeDriver();
            case FIREFOX -> createFirefoxDriver();
            case SAFARI -> createSafariDriver();
        };
    }

    private static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        if (BrowserParameters.isBrowserHeadless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        if (BrowserParameters.isBrowserHeadless()) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }

    private static WebDriver createSafariDriver() {
        return new SafariDriver(new SafariOptions());
    }
}
