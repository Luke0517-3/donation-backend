package com.irent.donation_backend.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irent.donation_backend.common.Constants;
import com.irent.donation_backend.config.LarkProperties;
import com.irent.donation_backend.model.*;
import com.irent.donation_backend.service.LarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LarkServiceImpl implements LarkService {

    private final LarkProperties larkProperties;
    private final ObjectMapper objectMapper;
    @Qualifier("authWebClient")
    private final WebClient authWebClient;
    @Qualifier("bitableWebClient")
    private final WebClient bitableWebClient;

    @Override
    public Mono<String> getTenantAccessToken() {
        return authWebClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "app_id", larkProperties.getAPP_ID(),
                        "app_secret", larkProperties.getAPP_SECRET()
                ))
                .retrieve()
                .bodyToMono(String.class)
                .handle((response, sink) -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(response);
                        sink.next(jsonNode.get("tenant_access_token").asText());
                    } catch (Exception e) {
                        sink.error(new RuntimeException("Failed to parse tenant access token", e));
                    }
                });
    }

    @Override
    public Mono<List<NGOEnvListItem>> listLarkBaseNGOEnv() {
        return getTenantAccessToken()
                .flatMap(token ->
                        bitableWebClient
                                .get()
                                .uri(uriBuilder -> uriBuilder
                                        .path("/{app_token}/tables/{table_id}/records")
                                        .build(
                                                larkProperties.getAPP_TOKEN(),
                                                larkProperties.getTABLE_ID()
                                        )
                                )
                                .header("Authorization", "Bearer " + token)
                                .retrieve()
                                .bodyToMono(String.class)
                                .map(this::parseItemsFromJson)
                );
    }

    @Override
    public Mono<Customer> getTargetStoreInfo(String path) {
        Map<String, Object> requestBody = createDynamicRequestBody("PATH", "is", List.of(path));
        return getTenantAccessToken()
                .flatMap(token ->
                        bitableWebClient
                                .post()
                                .uri(uriBuilder -> uriBuilder
                                        .path("/{app_token}/tables/{table_id}/records/search")
                                        .build(
                                                larkProperties.getAPP_TOKEN(),
                                                larkProperties.getTABLE_ID()
                                        )
                                )
                                .header("Authorization", "Bearer " + token)
                                .bodyValue(requestBody)
                                .retrieve()
                                .bodyToMono(String.class)
                                .map(result -> {
                                    try {
                                        JsonNode jsonNode = objectMapper.readTree(result);
                                        String msg = jsonNode.get("msg").asText();
                                        JsonNode itemsJsonNode = jsonNode.get("data").get("items");
                                        List<Customer> customers = itemsJsonNode.isArray() ? objectMapper.convertValue(itemsJsonNode,
                                                new TypeReference<>() {}) : Collections.emptyList();
                                        if (Constants.SUCCESS_MSG.equals(msg) &&
                                            !customers.isEmpty()
                                        )
                                            return customers.get(0);
                                        throw new RuntimeException("getTargetStoreInfo execute error");
                                    } catch (JsonProcessingException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                );
    }

    @Override
    public Mono<String> createDonationOrder(NGOOrderFields orderFields) {
        Map<String, Object> requestBody = Map.of("fields", orderFields);
        return getTenantAccessToken()
                .flatMap(token ->
                        bitableWebClient
                                .post()
                                .uri(uriBuilder -> uriBuilder
                                        .path("/{app_token}/tables/{table_id}/records")
                                        .build(
                                                larkProperties.getAPP_TOKEN(),
                                                larkProperties.getORDER_TABLE_ID()
                                        )
                                )
                                .header("Authorization", "Bearer " + token)
                                .bodyValue(requestBody)
                                .retrieve()
                                .bodyToMono(String.class)
                                .handle((result, sink) -> {
                                    try {
                                        JsonNode root = objectMapper.readTree(result);
                                        sink.next(root.get("msg").asText());
                                    } catch (JsonProcessingException e) {
                                        sink.error(new RuntimeException(e));
                                    }
                                })
                );
    }

    private List<NGOEnvListItem> parseItemsFromJson(String jsonString) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode itemsNode = rootNode.path("data").path("items");

            return objectMapper.readValue(itemsNode.toString(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }

    private Map<String, Object> createDynamicRequestBody(
            String fieldName,
            String operator,
            List<String> values
    ) {
        // 參數驗證
        Objects.requireNonNull(fieldName, "fieldName cannot be null");
        Objects.requireNonNull(operator, "operator cannot be null");
        Objects.requireNonNull(values, "values cannot be null");

        // 創建條件
        Map<String, Object> condition = Map.of(
                "field_name", fieldName,
                "operator", operator,
                "value", values
        );

        // 創建過濾器和請求體
        return Map.of("filter", Map.of(
                "conditions", List.of(condition),
                "conjunction", "and"
        ));
    }
}
