package com.irent.donation_backend.api;

import com.irent.donation_backend.model.Customer;
import com.irent.donation_backend.model.LarkResponse;
import com.irent.donation_backend.model.NGOEnvItem;
import com.irent.donation_backend.model.NGOOrderFields;
import com.irent.donation_backend.service.DonationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class DonationHandler {

    private final DonationService donationService;

    // 獲取捐款對象資訊
    public Mono<ServerResponse> getCustomer(ServerRequest request) {
        String name = request.pathVariable("name");
        return donationService.queryStoreInfo(name).flatMap(fields ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(Mono.just(fields), NGOEnvItem.class)
                )
                .switchIfEmpty(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> test(ServerRequest request) {
        return request.bodyToMono(NGOOrderFields.class)
                .flatMap(orderFields -> donationService.test(orderFields))
                .flatMap(result ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(Mono.just(Map.of("result", result)), Map.class)
                )
                .switchIfEmpty(ServerResponse.noContent().build())
                .onErrorResume(ex ->
                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Mono.just(Map.of("error", ex.getMessage())), Map.class)
                );
    }
}
