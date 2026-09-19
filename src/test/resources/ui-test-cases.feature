@e2e @ui @payment-page @checkout_solutions
Feature: Complete payment on hosted payment page
  As a customer
  I want to pay on a Solidgate-hosted payment page
  So that the merchant receives a successful payment confirmation

  Background:
    Given merchant API credentials are configured
    And test card details are configured

  @TC-UI-1 @positive @e2e @payment
  Scenario Outline: Complete payment on hosted page in multiple browsers
    Given a payment page is created via API for a standard order
    And I open the payment page using the response url
    When I fill in the payment form with:
      | field       | value            |
      | card_number | 4067429974719265 |
      | expiry      | 12/28            |
      | cvv         | 222              |
      | email       | test@example.com |
    And I submit the payment
    Then I should see the payment success message "Payment successful!"
    And the success page should show order description "Premium package"
    And the success page should show amount "€10.20"
    And the success page should show order title "Order Title"
    And the order status should have amount 1020
    And the order status should have currency "EUR"
    And the order status should be "auth_ok"

    Examples:
      | browser |
      | CHROME  |
      | FIREFOX |
