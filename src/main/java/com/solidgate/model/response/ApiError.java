package com.solidgate.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiError {

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    @JsonDeserialize(using = ApiErrorMessageDeserializer.class)
    private ApiErrorMessage message;

    @JsonProperty("messages")
    private List<String> messages;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ApiErrorMessage getMessage() {
        return message;
    }

    public void setMessage(ApiErrorMessage message) {
        this.message = message;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public String getMessageAsText() {
        if (message != null && !message.asText().isBlank()) {
            return message.asText();
        }
        if (messages != null && !messages.isEmpty()) {
            return String.join(", ", messages);
        }
        return "";
    }
}
