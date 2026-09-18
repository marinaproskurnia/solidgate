# Solidgate API & UI Tests

Automated tests for Solidgate Payment Page v1: API (`POST /init`) and end-to-end payment on the hosted page. Built with Java 21, JUnit 5, RestAssured, and Selenium 4.

## Prerequisites

- Java 21
- Maven 3.8+
- Chrome and/or Firefox (Safari on macOS only for UI tests)

## Configuration

Tests authenticate using Solidgate API v1 HMAC signatures:

- `merchant.public.key` → sent as the `merchant` request header
- `signature.secret.key` → used to compute the `signature` request header

Configure credentials in [`src/test/resources/testing.properties`](src/test/resources/testing.properties):

```properties
merchant.public.key=api_pk_...
signature.secret.key=api_sk_...
payment.page.base.url=https://payment-page.solidgate.com/api/v1
payment.page.link.base.url=https://payment-page.solidgate.com/link
browser.headless=false
```

Values are read via `PropertyReader` → `ApiParameters` / `BrowserParameters`.

Card number, CVV, email, and success message live in [`TestData`](src/test/java/com/solidgate/api/checkout_solutions/payment_page/TestData.java). Card expiry is generated at runtime (month `12`, year = current year + 2).

## Running tests

Full suite:

```bash
mvn test
```

API tests only:

```bash
mvn test -Dtest=com.solidgate.api.checkout_solutions.payment_page.*
```

UI E2E test only (runs in Chrome, Firefox, and Safari on macOS):

```bash
mvn test -Dtest=CreatePaymentPagePaymentUiTest
```

Single API test class:

```bash
mvn test -Dtest=CreatePaymentPagePaymentTest
```

Tests are skipped automatically when `merchant.public.key` or `signature.secret.key` are not configured.

## Test structure

```
src/main/java/com/solidgate/config/
  PropertyReader.java                 # loads testing.properties
  ApiParameters.java                  # merchant keys, payment page URLs
  BrowserParameters.java              # browser.headless

src/test/java/com/solidgate/api/checkout_solutions/payment_page/
  CreatePaymentPagePaymentTest.java   # TC1, TC4–TC9
  PaymentPageApiSteps.java            # reusable API steps for UI tests
  PaymentPageRequestFactory.java
  BaseApiTest.java
  TestData.java                       # shared / API / UI fixtures

src/test/java/com/solidgate/ui/checkout_solutions/payment_page/
  CreatePaymentPagePaymentUiTest.java # TC10 — E2E in Chrome, Firefox, Safari
  BaseUiTest.java
  helpers/                            # WebDriverFactory, CardExpiryGenerator
  pages/                              # PaymentCheckoutPage, PaymentSuccessPage
```

BDD scenarios: [`src/test/resources/test-cases.feature`](src/test/resources/test-cases.feature)

## Troubleshooting

- **Tests skipped** — set `merchant.public.key` and `signature.secret.key` in `testing.properties`.
- **Authentication failed (`1.01`)** — verify keys match your Solidgate sandbox channel and that the exact JSON body is used for signing.
- **UI test timeout** — ensure browsers are installed; set `browser.headless=true` for CI.
- **Safari skipped** — Safari runs only on macOS with Safari and `safaridriver` enabled.
