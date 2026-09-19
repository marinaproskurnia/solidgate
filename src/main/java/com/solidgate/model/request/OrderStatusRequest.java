package com.solidgate.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderStatusRequest {

    @JsonProperty("order_id")
    private final String orderId;

    public OrderStatusRequest(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
