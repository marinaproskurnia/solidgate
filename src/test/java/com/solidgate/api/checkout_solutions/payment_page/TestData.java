package com.solidgate.api.checkout_solutions.payment_page;

public final class TestData {

    public static final String ORDER_TYPE_AUTH = "auth";
    public static final String ORDER_DESCRIPTION = "Premium package";
    public static final String PUBLIC_NAME = "Public Name";
    public static final String ORDER_TITLE = "Order Title";
    public static final String PAYMENT_CURRENCY = "EUR";
    public static final int PAYMENT_AMOUNT = 1020;
    public static final String EXPECTED_ORDER_STATUS = "auth_ok";

    public static final String TEST_CARD_NUMBER = "4067429974719265";
    public static final String TEST_CARD_CVV = "222";
    public static final String TEST_CARD_EMAIL = "test@example.com";
    public static final String TEST_CARD_EXPIRY_MONTH = "12";
    public static final String PAYMENT_SUCCESS_MESSAGE = "Payment successful!";
    public static final String EXPECTED_PAYMENT_AMOUNT_DISPLAY = "€10.20";

    private TestData() {}
}
