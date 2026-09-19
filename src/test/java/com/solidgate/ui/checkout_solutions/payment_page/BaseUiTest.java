package com.solidgate.ui.checkout_solutions.payment_page;

import com.solidgate.config.ApiParameters;
import com.solidgate.model.web.BrowserType;
import com.solidgate.ui.checkout_solutions.payment_page.helpers.WebDriverFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

import static io.restassured.RestAssured.filters;

abstract class BaseUiTest {

    protected WebDriver driver;

    @BeforeAll
    static void registerApiLogging() {
        filters(new AllureRestAssured(),
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );
    }

    @BeforeEach
    void requireCredentials() {
        Assumptions.assumeTrue(
                ApiParameters.hasCredentials(),
                "Configure merchant.public.key and signature.secret.key in testing.properties"
        );
    }

    protected void startBrowser(BrowserType browserType) {
        driver = WebDriverFactory.create(browserType);
        driver.manage().window().maximize();
        Allure.getLifecycle().updateTestCase(testCase -> testCase.setName(
                testCase.getName() + " [" + browserType.name() + "]"
        ));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
