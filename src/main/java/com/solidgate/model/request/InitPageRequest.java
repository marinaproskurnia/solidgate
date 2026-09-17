package com.solidgate.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InitPageRequest {

    @JsonProperty("order")
    private Order order;

    @JsonProperty("page_customization")
    private PageCustomization pageCustomization;

    public InitPageRequest() {
    }

    public InitPageRequest(Order order, PageCustomization pageCustomization) {
        this.order = order;
        this.pageCustomization = pageCustomization;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public PageCustomization getPageCustomization() {
        return pageCustomization;
    }

    public void setPageCustomization(PageCustomization pageCustomization) {
        this.pageCustomization = pageCustomization;
    }
}
