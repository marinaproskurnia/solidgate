package com.solidgate.api.checkout_solutions.payment_page.steps;

import com.solidgate.api.checkout_solutions.payment_page.data.TestData;
import com.solidgate.model.request.InitPageRequest;
import com.solidgate.model.request.Order;
import com.solidgate.model.request.PageCustomization;

import java.util.UUID;

public final class PaymentPageRequestFactory {

    private PaymentPageRequestFactory() {}

    public static InitPageRequest paymentRequest(String orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setAmount(TestData.PAYMENT_AMOUNT);
        order.setCurrency(TestData.PAYMENT_CURRENCY);
        order.setOrderDescription(TestData.ORDER_DESCRIPTION);
        order.setType(TestData.ORDER_TYPE_AUTH);

        PageCustomization pageCustomization = new PageCustomization(TestData.PUBLIC_NAME);
        pageCustomization.setOrderTitle(TestData.ORDER_TITLE);
        pageCustomization.setOrderDescription(TestData.ORDER_DESCRIPTION);

        return new InitPageRequest(order, pageCustomization);
    }

    public static InitPageRequest subscriptionRequest(String orderId, String productId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setProductId(productId);
        order.setCustomerAccountId(UUID.randomUUID().toString());
        order.setOrderDescription(TestData.ORDER_DESCRIPTION);
        order.setType(TestData.ORDER_TYPE_AUTH);

        return new InitPageRequest(order, new PageCustomization(TestData.PUBLIC_NAME));
    }

    public static InitPageRequest invoiceRequest(String orderId, String invoiceId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setInvoiceId(invoiceId);
        order.setOrderDescription(TestData.ORDER_DESCRIPTION);

        return new InitPageRequest(order, new PageCustomization(TestData.PUBLIC_NAME));
    }
}
