# Solidgate API & UI Tests

Automated tests for Solidgate Payment Page v1 and a follow-up card-order status check. 
Built with Java 21, JUnit 5, RestAssured, Selenium 4, AssertJ, Allure.

## Prerequisites
- Java 21
- Maven 3.8+
- Chrome and Firefox browsers are installed

Before running tests set 'merchant.public.key' and 'signature.secret.key' either in 
properties file: [`src/test/resources/testing.properties`](src/test/resources/testing.properties)
or pass them via env var, i.e. '-Dmerchant.public.key='

## Running tests

1. Full suite (including unit tests):
```bash
mvn clean test
```

2. API tests only:

```bash
mvn clean test -Dtest=CreatePaymentPagePaymentApiTest
```

3. UI test only (in Chrome and Firefox):

```bash
mvn clean test -Dtest=CreatePaymentPagePaymentUiTest
```

## Test results
Test results will be in `target/allure-results/`. Generate and open Allure report via starting Web server with results:
```
mvn allure:serve
```
