package com.solidgate.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solidgate.model.response.ApiError;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ApiError} Jackson custom deserialization
 */
class ApiErrorDeserializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldDeserializeValidationErrorForMissingAmount() throws Exception {
        ApiError error = deserialize("""
                {
                  "code": "2.01",
                  "message": {
                    "order": {
                      "amount": "cannot be blank"
                    }
                  }
                }
                """);

        assertThat(error.getCode()).isEqualTo("2.01");
        assertThat(error.getMessage().isValidation()).isTrue();
        assertThat(error.getMessage().getValidation().getOrderFieldError("amount"))
                .isEqualTo("cannot be blank");
    }

    @Test
    void shouldDeserializeValidationErrorForInvalidAmount() throws Exception {
        ApiError error = deserialize("""
                {
                  "code": "2.01",
                  "message": {
                    "order": {
                      "amount": "must be greater than or equal to 0"
                    }
                  }
                }
                """);

        assertThat(error.getMessage().getValidation().getOrderFieldError("amount"))
                .isEqualTo("must be greater than or equal to 0");
    }

    @Test
    void shouldDeserializeValidationErrorForMissingPublicName() throws Exception {
        ApiError error = deserialize("""
                {
                  "code": "2.01",
                  "message": {
                    "page_customization": {
                      "public_name": "cannot be blank"
                    }
                  }
                }
                """);

        assertThat(error.getMessage().getValidation().getPageCustomizationFieldError("public_name"))
                .isEqualTo("cannot be blank");
    }

    @Test
    void shouldDeserializeListErrorMessage() throws Exception {
        ApiError error = deserialize("""
                {
                  "code": "2.01",
                  "message": [
                    "Invoice not found"
                  ]
                }
                """);

        assertThat(error.getMessage().isList()).isTrue();
        assertThat(error.getMessage().getList()).containsExactly("Invoice not found");
    }

    @Test
    void shouldDeserializeAuthenticationErrorMessagesField() throws Exception {
        ApiError error = deserialize("""
                {
                  "code": "1.01",
                  "messages": [
                    "Authentication failed"
                  ]
                }
                """);

        assertThat(error.getMessages()).containsExactly("Authentication failed");
        assertThat(error.getMessageAsText()).isEqualTo("Authentication failed");
    }

    private ApiError deserialize(String json) throws Exception {
        return objectMapper.readValue(json, ApiError.class);
    }
}
