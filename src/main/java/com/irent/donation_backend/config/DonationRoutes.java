package com.irent.donation_backend.config;

import com.irent.donation_backend.api.DonationHandler;
import com.irent.donation_backend.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class DonationRoutes {

    private static final String MAIN_REQUEST_MAPPING = "/api/donation";

    @Bean
    public DonationHandler donationHandler(DonationService donationService) {
        return new DonationHandler(donationService);
    }

    @Bean
    public RouterFunction<ServerResponse> donationRouterFunction(DonationHandler handler) {
        return RouterFunctions.route()
                .path(MAIN_REQUEST_MAPPING,
                        builder -> builder
                                .GET("/customer/{name}", handler::getCustomer)
                )
                .build();
    }
}
