package com.irent.donation_backend.service.impl;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.irent.donation_backend.common.Constants;
import com.irent.donation_backend.config.LarkProperties;
import com.irent.donation_backend.model.lark.Customer;
import com.irent.donation_backend.model.lark.NGOEnvListItem;
import com.irent.donation_backend.model.lark.NGOOrderFields;
import com.irent.donation_backend.model.newebpay.OrderInfoDTO;
import com.irent.donation_backend.service.LarkService;
import com.irent.donation_backend.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class LarkServiceImpl implements LarkService {

    private static final String APP_TOKEN_PATH = "/{app_token}/tables/{table_id}/records";
    private static final String BEARER_PREFIX = "Bearer ";

    private final LarkProperties larkProperties;
    private final JsonUtils jsonUtils;
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
                .map(response -> handleJsonResponse(response,
                        jsonNode -> jsonNode.get("tenant_access_token").asText()));
    }

    @Override
    public Mono<List<NGOEnvListItem>> listLarkBaseNGOEnv() {
        return executeRequest(
                APP_TOKEN_PATH,
                HttpMethod.GET,
                null,
                jsonNode -> parseItemsFromJson(jsonNode.get("data").get("items").toString())
        );
    }

    @Override
    public Mono<Customer> getTargetStoreInfo(String path) {
        Map<String, Object> requestBody = createDynamicRequestBody("PATH", "is", List.of(path));
        return executeRequest(
                APP_TOKEN_PATH + "/search",
                HttpMethod.POST,
                requestBody,
                jsonNode -> {
                    String msg = jsonNode.get("msg").asText();
                    if (!Constants.SUCCESS_MSG.equals(msg)) {
                        throw new RuntimeException("API returned error: " + msg);
                    }
                    JsonNode itemsNode = jsonNode.get("data").get("items");
                    List<Customer> customers = itemsNode.isArray()
                            ? jsonUtils.convertNodeToType(itemsNode, new TypeReference<>() {
                    })
                            : Collections.emptyList();
                    return customers.stream()
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("No customer found"));
                }
        );
    }

    @Override
    public Mono<OrderInfoDTO> createDonationOrder(NGOOrderFields orderFields) {
        String orderId = generateOrderId();
        orderFields.setOrderId(orderId);
        Map<String, Object> requestBody = Map.of("fields", orderFields);

        return executeRequest(
                APP_TOKEN_PATH,
                HttpMethod.POST,
                requestBody,
                jsonNode -> {
                    String msg = jsonNode.get("msg").asText();

                    // 檢查訂單創建是否成功
                    if (!Constants.SUCCESS_MSG.equals(msg)) {
                        throw new RuntimeException("訂單建立失敗: " + msg + " orderFields: " + orderFields);
                    }

                    return OrderInfoDTO.builder()
                            .orderId(orderId)
                            .amount(Integer.valueOf(orderFields.getAmount()))
                            .itemDesc("愛心捐款")
                            .email(orderFields.getEmail())
                            .build();
                }
        );
    }

    private String generateOrderId() {
        return NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR,
                NanoIdUtils.DEFAULT_ALPHABET,
                15) + "_" + System.currentTimeMillis() / 1000;
    }

    private <T> T handleJsonResponse(String response, Function<JsonNode, T> processor) {
        return processor.apply(jsonUtils.parseJsonToNode(response));
    }

    private <T> Mono<T> executeRequest(
            String path,
            HttpMethod method,
            Object body,
            Function<JsonNode, T> responseProcessor
    ) {
        return getTenantAccessToken()
                .flatMap(token -> {
                    WebClient.RequestBodySpec request = bitableWebClient
                            .method(method)
                            .uri(uriBuilder -> uriBuilder
                                    .path(path)
                                    .build(
                                            larkProperties.getAPP_TOKEN(),
                                            method == HttpMethod.POST && body instanceof Map<?, ?> bodyMap
                                                    && bodyMap.containsKey("fields")
                                                    ? larkProperties.getORDER_TABLE_ID()
                                                    : larkProperties.getTABLE_ID()
                                    ))
                            .header("Authorization", BEARER_PREFIX + token);

                    if (body != null) {
                        request.bodyValue(body);
                    }

                    return request
                            .retrieve()
                            .bodyToMono(String.class)
                            .map(response -> handleJsonResponse(response, responseProcessor));
                });
    }

    private List<NGOEnvListItem> parseItemsFromJson(String jsonString) {
        return jsonUtils.parseJson(jsonString, new TypeReference<>() {
        });
    }

    private Map<String, Object> createDynamicRequestBody(
            String fieldName,
            String operator,
            List<String> values
    ) {
        Objects.requireNonNull(fieldName, "fieldName cannot be null");
        Objects.requireNonNull(operator, "operator cannot be null");
        Objects.requireNonNull(values, "values cannot be null");

        return Map.of("filter", Map.of(
                "conditions", List.of(
                        Map.of(
                                "field_name", fieldName,
                                "operator", operator,
                                "value", values
                        )
                ),
                "conjunction", "and"
        ));
    }
}