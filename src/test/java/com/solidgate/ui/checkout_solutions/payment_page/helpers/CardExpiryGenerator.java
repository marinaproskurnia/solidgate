package com.solidgate.ui.checkout_solutions.payment_page.helpers;

import java.time.Year;

import static com.solidgate.api.checkout_solutions.payment_page.TestData.TEST_CARD_EXPIRY_MONTH;
import static java.lang.String.format;

public final class CardExpiryGenerator {

    private CardExpiryGenerator() {}

    public static String generate() {
        int expiryYear = getExpiryYearInFuture();
        String yearSuffix = format("%02d", expiryYear % 100);
        return String.format("%s/%s", TEST_CARD_EXPIRY_MONTH, yearSuffix);
    }

    private static int getExpiryYearInFuture() {
        var currentYear = Year.now();
        return currentYear.getValue() + 2;
    }
}
