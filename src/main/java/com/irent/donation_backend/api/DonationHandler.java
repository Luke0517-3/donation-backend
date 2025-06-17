package com.irent.donation_backend.api;

import com.irent.donation_backend.model.Customer;
import com.irent.donation_backend.service.DonationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class DonationHandler {

    private final DonationService donationService;

    // 獲取捐款對象資訊
    public Mono<ServerResponse> getCustomer(ServerRequest request) {
        String name = request.pathVariable("name");
        return Mono.fromSupplier(() -> donationService.queryCustomer(name))
                .flatMap(customer ->
                        ServerResponse.ok()
                                .body(Mono.just(customer), Customer.class)
                )
                .log(log.getName());
    }
}
