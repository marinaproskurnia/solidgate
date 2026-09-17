package com.solidgate.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationMessage {

    @JsonProperty("order")
    private Map<String, String> order;

    @JsonProperty("page_customization")
    private Map<String, String> pageCustomization;

    public Map<String, String> getOrder() {
        return order == null ? Collections.emptyMap() : order;
    }

    public void setOrder(Map<String, String> order) {
        this.order = order;
    }

    public Map<String, String> getPageCustomization() {
        return pageCustomization == null ? Collections.emptyMap() : pageCustomization;
    }

    public void setPageCustomization(Map<String, String> pageCustomization) {
        this.pageCustomization = pageCustomization;
    }

    public String getOrderFieldError(String field) {
        return getOrder().get(field);
    }

    public String getPageCustomizationFieldError(String field) {
        return getPageCustomization().get(field);
    }
}
