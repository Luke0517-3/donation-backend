package com.irent.donation_backend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irent.donation_backend.config.LarkProperties;
import com.irent.donation_backend.service.LarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LarkServiceImpl implements LarkService {

    private final WebClient.Builder webClientBuilder;
    private final LarkProperties larkProperties;
    private final ObjectMapper objectMapper;
    private volatile WebClient webClient;

    private WebClient getWebClient() {
        if (webClient == null) {
            synchronized (this) {
                if (webClient == null) {
                    webClient = webClientBuilder
                            .baseUrl(larkProperties.getAUTH_URL())
                            .build();
                }
            }
        }
        return webClient;
    }

    @Override
    public Mono<String> getTenantAccessToken() {
        return getWebClient().post()
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
}
