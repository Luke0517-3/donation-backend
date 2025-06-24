package com.irent.donation_backend.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irent.donation_backend.config.LarkProperties;
import com.irent.donation_backend.model.NGOEnvItem;
import com.irent.donation_backend.service.LarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public Mono<List<NGOEnvItem>> listLarkBaseNGOEnv() {
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

    private List<NGOEnvItem> parseItemsFromJson(String jsonString) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode itemsNode = rootNode.path("data").path("items");

            return objectMapper.readValue(
                    itemsNode.toString(),
                    new TypeReference<List<NGOEnvItem>>() {}
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }
}
