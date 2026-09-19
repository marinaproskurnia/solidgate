@api @payment-page @checkout_solutions
Feature: Create Payment Page via Solidgate API v1
  As a merchant
  I want to create a hosted payment page through POST /init
  So that customers can complete payment on a Solidgate-hosted page

  API reference: https://api-docs.solidgate.com/api/payment-page/create-payment-page
  Endpoint: POST https://payment-page.solidgate.com/api/v1/init
  Authentication: merchant (public key) and signature (HMAC-SHA512) headers

  Background:
    Given merchant API credentials are configured
    And the payment page base URL is "https://payment-page.solidgate.com/api/v1"

  @TC1 @positive @api @payment
  Scenario: Create payment page for a standard order
    Given a unique merchant order id
    And a payment order with:
      | field              | value             |
      | amount             | 1020              |
      | currency           | EUR               |
      | order_description  | Premium package   |
      | type               | auth              |
    And page customization with:
      | field              | value             |
      | public_name        | Public Name       |
      | order_title        | Order Title       |
      | order_description  | Premium package   |
    When I send a signed POST /init request to create a payment page
    Then the response should not contain an error
    And the response should contain a payment page url starting with "https://payment-page.solidgate.com/"
    And the response should contain a non-empty payment page id
    And the payment page url should end with the payment page id
    And if the response contains guid it should be non-empty

  @TC2 @positive @api @subscription @not_implemented
  Scenario: Create payment page for a subscription order
    # Skipped — no automated implementation yet
    Given a unique merchant order id
    And a unique product identifier of the subscription product_id
    And a unique customer account id
    And a subscription order with:
      | field              | value             |
      | order_description  | Premium package   |
      | type               | auth              |
    And page customization with public name "Public Name"
    When I send a signed POST /init request to create a payment page
    Then the response should not contain an error
    And the response should contain a payment page url starting with "https://payment-page.solidgate.com/"
    And the response should contain a non-empty payment page id
    And the payment page url should end with the payment page id
    And if the response contains guid it should be non-empty

  @TC3 @positive @api @invoice @not_implemented
  Scenario: Create payment page for an invoice order
    # Skipped — no automated implementation yet
    Given a unique merchant order id
    And a unique invoice_id
    And an invoice order with:
      | field              | value             |
      | order_description  | Premium package   |
    And page customization with public name "Public Name"
    When I send a signed POST /init request to create a payment page
    Then the response should not contain an error
    And the response should contain a payment page url starting with "https://payment-page.solidgate.com/"
    And the response should contain a non-empty payment page id
    And the payment page url should end with the payment page id
    And if the response contains guid it should be non-empty

  @TC4 @negative @api @authentication
  Scenario: Fail to create payment page with invalid signature
    Given a unique merchant order id
    And a valid payment order request body
    When I send POST /init with an invalid signature
    Then the response should contain an error
    And the error code should be "1.01"
    And the error message should contain "Authentication failed"

  @TC5 @negative @api @validation
  Scenario: Fail to create payment page with invalid amount
    Given a unique merchant order id
    And a payment order with:
      | field              | value             |
      | amount             | -1                |
      | currency           | EUR               |
      | order_description  | Premium package   |
    And page customization with public name "Public Name"
    When I send a signed POST /init request to create a payment page
    Then the response should contain an error
    And the error code should be "2.01"
    And the validation error for order field "amount" should be "must be greater than or equal to 0"

  @TC6 @negative @api @validation
  Scenario: Fail to create payment page when required currency is missing
    Given a unique merchant order id
    And a payment order request body without amount and currency
    And page customization with public name "Public Name"
    When I send a signed POST /init request to create a payment page
    Then the response should contain an error
    And the error code should be "2.01"
    And the validation error for order field "currency" should be "cannot be blank"

  @TC7 @negative @api @validation
  Scenario: Fail to create payment page when required public name is missing
    Given a unique merchant order id
    And a payment order with:
      | field              | value             |
      | amount             | 1020              |
      | currency           | EUR               |
      | order_description  | Premium package   |
    And page customization without public name
    When I send a signed POST /init request to create a payment page
    Then the response should contain an error
    And the error code should be "2.01"
    And the validation error for page_customization field "public_name" should be "cannot be blank"

  @TC8 @negative @api @invoice
  Scenario: Fail to create payment page when invoice id does not exist
    Given a unique merchant order id
    And an invoice order with invoice id "inv_nonexisting_value"
    And page customization with public name "Public Name"
    When I send a signed POST /init request to create a payment page
    Then the response should contain an error
    And the error code should be "2.01"
    And the error message list should contain "Invoice not found"

  @TC9 @negative @api @subscription
  Scenario: Fail to create subscription payment page when product id does not exist
    Given a unique merchant order id
    And a unique customer account id
    And a subscription order with a non-existing product id (random UUID)
    And page customization with public name "Public Name"
    When I send a signed POST /init request to create a payment page
    Then the response should contain an error
    And the error code should be "2.01"
    And the error message list should contain "Subscription error"
