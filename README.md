# Solidgate API & UI Tests

Automated tests for Solidgate Payment Page v1 and a follow-up card-order status check. 
Built with Java 21, JUnit 5, RestAssured, Selenium 4, AssertJ, Allure.

## Prerequisites
- Java 21
- Maven 3.8+
- Chrome and Firefox browsers are installed

## Configuration
Properties: [`src/test/resources/testing.properties`](src/test/resources/testing.properties)

## Running tests

1. Full suite (covers also unit tests):
```bash
mvn clean test
```

2. API tests only:

```bash
mvn clean test -Dtest=CreatePaymentPagePaymentApiTest
```

3. UI test (in Chrome and Firefox):

```bash
mvn clean test -Dtest=CreatePaymentPagePaymentUiTest
```

## Test results
Test results are under `target/allure-results/`.
Generate and open Allure report via starting Web server with results:
```
mvn allure:serve
```
