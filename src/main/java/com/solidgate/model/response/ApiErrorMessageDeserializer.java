package com.solidgate.model.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiErrorMessageDeserializer extends JsonDeserializer<ApiErrorMessage> {

    @Override
    public ApiErrorMessage deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        JsonNode node = mapper.readTree(parser);

        if (node.isArray()) {
            return ApiErrorMessage.list(readStringList(node));
        }

        if (node.isObject()) {
            ValidationMessage validation = mapper.treeToValue(node, ValidationMessage.class);
            return ApiErrorMessage.validation(validation);
        }

        if (node.isTextual()) {
            return ApiErrorMessage.list(List.of(node.asText()));
        }

        return null;
    }

    private List<String> readStringList(JsonNode node) {
        List<String> messages = new ArrayList<>();
        node.forEach(element -> messages.add(element.asText()));
        return messages;
    }
}
