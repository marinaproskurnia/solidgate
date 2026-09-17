package com.solidgate.checkout_solutions.payment_page;

final class TestData {//here

    // In production this should be retrieved from the invoice-creation endpoint.
    // Hardcoded here for Phase 1 API tests.
    static final String INVOICE_ID = "inv_01KPWT0K1PQWEYYRT7J1H3NC85";
    static final String ORDER_TYPE_AUTH = "auth";

    static final String ORDER_DESCRIPTION = "Premium package";
    static final String PUBLIC_NAME = "Public Name";
    static final int PAYMENT_AMOUNT = 1020;
    static final String PAYMENT_CURRENCY = "EUR";

    private TestData() {
    }
}
