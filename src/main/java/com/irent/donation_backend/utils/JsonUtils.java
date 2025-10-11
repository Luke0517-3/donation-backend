package com.irent.donation_backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonUtils {

    @Getter
    private final ObjectMapper objectMapper;

    public <T> T parseJson(String jsonString, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(jsonString, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }

    public JsonNode parseJsonToNode(String jsonString) {
        try {
            return objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON to JsonNode", e);
        }
    }

    public <T> T convertNodeToType(JsonNode node, TypeReference<T> typeReference) {
        return objectMapper.convertValue(node, typeReference);
    }
}
