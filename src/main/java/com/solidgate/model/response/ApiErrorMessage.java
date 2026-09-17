package com.solidgate.model.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiErrorMessage {

    private final ValidationMessage validation;
    private final List<String> list;

    private ApiErrorMessage(ValidationMessage validation, List<String> list) {
        this.validation = validation;
        this.list = list;
    }

    public static ApiErrorMessage validation(ValidationMessage validation) {
        return new ApiErrorMessage(validation, null);
    }

    public static ApiErrorMessage list(List<String> list) {
        return new ApiErrorMessage(null, list);
    }

    public boolean isValidation() {
        return validation != null;
    }

    public boolean isList() {
        return list != null;
    }

    public ValidationMessage getValidation() {
        return validation;
    }

    public List<String> getList() {
        return list == null ? Collections.emptyList() : list;
    }

    public String asText() {
        if (isValidation()) {
            List<String> parts = new ArrayList<>();
            validation.getOrder().forEach((field, error) -> parts.add("order." + field + ": " + error));
            validation.getPageCustomization()
                    .forEach((field, error) -> parts.add("page_customization." + field + ": " + error));
            return String.join(", ", parts);
        }
        if (isList()) {
            return String.join(", ", list);
        }
        return "";
    }
}
