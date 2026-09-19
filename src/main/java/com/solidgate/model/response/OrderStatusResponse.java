package com.solidgate.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderStatusResponse {

    @JsonProperty("order")
    private StatusOrder order;

    public StatusOrder getOrder() {
        return order;
    }

    public void setOrder(StatusOrder order) {
        this.order = order;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StatusOrder {

        @JsonProperty("order_id")
        private String orderId;

        @JsonProperty("amount")
        private Integer amount;

        @JsonProperty("currency")
        private String currency;

        @JsonProperty("status")
        private String status;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
