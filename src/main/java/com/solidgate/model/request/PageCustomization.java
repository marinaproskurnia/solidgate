package com.solidgate.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PageCustomization {

    @JsonProperty("public_name")
    private String publicName;

    @JsonProperty("order_title")
    private String orderTitle;

    @JsonProperty("order_description")
    private String orderDescription;

    public PageCustomization() {
    }

    public PageCustomization(String publicName) {
        this.publicName = publicName;
    }

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }
}
