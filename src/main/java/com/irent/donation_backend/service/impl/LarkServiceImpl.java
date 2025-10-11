package com.irent.donation_backend.service.impl;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class LarkServiceImpl implements LarkService {

    private static final String APP_TOKEN_PATH = "/{app_token}/tables/{table_id}/records";
    private static final String RECORD_PATH = "/{app_token}/tables/{table_id}/records/{record_id}";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final Logger log = LoggerFactory.getLogger(LarkServiceImpl.class);

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
        Map<String, Object> requestBody = Map.of(
                "filter", Map.of(
                        "conditions", List.of(Map.of("field_name", "PATH", "operator", "is", "value", List.of(path))),
                        "conjunction", "and"
                )
        );

        return executeRequest(
                APP_TOKEN_PATH + "/search",
                HttpMethod.POST,
                requestBody,
                jsonNode -> {
                    validateApiResponse(jsonNode, "API returned error");
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
        orderFields.setPayStatus(0);
        Map<String, Object> requestBody = Map.of("fields", orderFields);

        return executeRequest(
                APP_TOKEN_PATH,
                HttpMethod.POST,
                requestBody,
                jsonNode -> {
                    validateApiResponse(jsonNode, "訂單建立失敗", orderFields);
                    JsonNode recordNode = jsonNode.get("data").get("record");
                    return recordNode.get("record_id").asText();
                }
        ).flatMap(recordId -> setOrderId(recordId)
                .thenReturn(OrderInfoDTO.builder()
                        .orderId(recordId)
                        .amount(orderFields.getAmount())
                        .itemDesc("愛心捐款")
                        .email(orderFields.getEmail())
                        .build())
        );
    }

    @Override
    public Mono<String> updateDonationOrder(String recordId, Integer payStatus) {
        Map<String, Object> requestBody = Map.of("fields", Map.of("PAY_STATUS", payStatus));

        return executeRequest(
                RECORD_PATH,
                HttpMethod.PUT,
                requestBody,
                jsonNode -> {
                    validateApiResponse(jsonNode, "訂單更新失敗", "recordId: " + recordId);
                    return Constants.SUCCESS;
                },
                larkProperties.getORDER_TABLE_ID(),
                recordId
        );
    }

    private Mono<String> setOrderId(String recordId) {
        Map<String, Object> requestBody = Map.of("fields", Map.of("ORDER_ID", recordId));

        return executeRequest(
                RECORD_PATH,
                HttpMethod.PUT,
                requestBody,
                jsonNode -> {
                    validateApiResponse(jsonNode, "訂單更新失敗", "recordId: " + recordId);
                    return Constants.SUCCESS;
                },
                larkProperties.getORDER_TABLE_ID(),
                recordId
        );
    }

    @Override
    public Mono<Object> queryOrderInfo(String recordId) {
        Map<String, Object> requestBody = Map.of(
                "filter", Map.of(
                        "conditions", List.of(Map.of("field_name", "ORDER_ID", "operator", "is", "value", List.of(recordId))),
                        "conjunction", "and"
                )
        );

        return executeRequest(
                APP_TOKEN_PATH + "/search",
                HttpMethod.POST,
                requestBody,
                jsonNode -> {
                    validateApiResponse(jsonNode, "訂單查詢失敗", "recordId: " + recordId);
                    JsonNode itemsNode = jsonNode.get("data").get("items");
                    List<Object> res = itemsNode.isArray()
                            ? jsonUtils.convertNodeToType(itemsNode, new TypeReference<>() {
                    })
                            : Collections.emptyList();
                    return res.stream()
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("No customer found"));
                },
                larkProperties.getORDER_TABLE_ID(),
                recordId
        );
    }

    private void validateApiResponse(JsonNode jsonNode, String errorMessage, Object... details) {
        String msg = jsonNode.get("msg").asText();
        if (!Constants.SUCCESS_MSG.equals(msg)) {
            log.error("Response indicates failure: {}", Optional.ofNullable(jsonUtils.convertNodeToType(jsonNode, new TypeReference<>() {
            })));
            throw new RuntimeException(errorMessage + ": " + msg +
                    (details.length > 0 ? " " + details[0] : ""));
        }
    }

    private <T> T handleJsonResponse(String response, Function<JsonNode, T> processor) {
        return processor.apply(jsonUtils.parseJsonToNode(response));
    }

    private <T> Mono<T> executeRequest(
            String pathTemplate,
            HttpMethod method,
            Object body,
            Function<JsonNode, T> responseProcessor,
            String tableId,
            Object... additionalPathVariables
    ) {
        return getTenantAccessToken()
                .flatMap(token -> {
                    Object[] pathVars = buildPathVariables(tableId, additionalPathVariables);

                    WebClient.RequestBodySpec request = bitableWebClient
                            .method(method)
                            .uri(uriBuilder -> uriBuilder.path(pathTemplate).build(pathVars))
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

    private Object[] buildPathVariables(String tableId, Object... additionalPathVariables) {
        int extraLength = additionalPathVariables != null ? additionalPathVariables.length : 0;
        Object[] pathVars = new Object[2 + extraLength];
        pathVars[0] = larkProperties.getAPP_TOKEN();
        pathVars[1] = tableId;

        if (extraLength > 0) {
            System.arraycopy(additionalPathVariables, 0, pathVars, 2, extraLength);
        }

        return pathVars;
    }

    private <T> Mono<T> executeRequest(
            String path,
            HttpMethod method,
            Object body,
            Function<JsonNode, T> responseProcessor
    ) {
        String tableId = method == HttpMethod.POST && body instanceof Map<?, ?> bodyMap
                && bodyMap.containsKey("fields")
                ? larkProperties.getORDER_TABLE_ID()
                : larkProperties.getTABLE_ID();

        return executeRequest(path, method, body, responseProcessor, tableId);
    }

    private List<NGOEnvListItem> parseItemsFromJson(String jsonString) {
        return jsonUtils.parseJson(jsonString, new TypeReference<>() {
        });
    }
}