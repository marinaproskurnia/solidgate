# Solidgate API Tests
//here - update in the end
Automated API tests for v.1 Solidgate Payment Page (`POST /init`), built with Java 21, JUnit 5, and RestAssured.

## Prerequisites

- Java 21
- Maven 3.8+

## Configuration

Tests authenticate using Solidgate API v1 HMAC signatures:

- `merchant.public.key` → sent as the `merchant` request header
- `signature.secret.key` → used to compute the `signature` request header

Configure credentials using one of these options (env vars take precedence):

### Option 1: Environment variables

```bash
export MERCHANT_PUBLIC_KEY="api_pk_..."
export SIGNATURE_SECRET_KEY="api_sk_..."
export PAYMENT_PAGE_BASE_URL="https://payment-page.solidgate.com/api/v1"
```

### Option 2: Local properties file

Create `src/test/resources/testing.local.properties` (gitignored):

```properties
merchant.public.key=api_pk_...
signature.secret.key=api_sk_...
payment.page.base.url=https://payment-page.solidgate.com/api/v1
```

Default values live in [`src/test/resources/testing.properties`](src/test/resources/testing.properties).

## Running tests

Full suite:

```bash
mvn test
```

Payment page tests only:

```bash
mvn test -Dtest=com.solidgate.checkout_solutions.payment_page.*
```

Single test class:

```bash
mvn test -Dtest=CreatePaymentPagePaymentTest
```

Tests are skipped automatically when `merchant.public.key` or `signature.secret.key` are not configured.

## Test structure

```
src/test/java/com/solidgate/checkout_solutions/payment_page/
  CreatePaymentPagePaymentTest.java       # standard order (amount + currency)
  CreatePaymentPageSubscriptionTest.java # subscription (product_id)
  CreatePaymentPageInvoiceTest.java       # invoice (invoice_id)
  CreatePaymentPageNegativeTest.java      # auth and validation failures
```

Hardcoded sandbox identifiers:

- `SUBSCRIPTION_PRODUCT_ID` — unique subscription product identifier
- `INVOICE_ID` — temporary hardcoded value; in production this should come from the invoice API

## Troubleshooting

- **Tests skipped** — set `merchant.public.key` and `signature.secret.key`.
- **Authentication failed (`1.01`)** — verify keys match your Solidgate sandbox channel and that the exact JSON body is used for signing.
- **Subscription/invoice failures** — confirm hardcoded `product_id` and `invoice_id` exist in your sandbox environment.
