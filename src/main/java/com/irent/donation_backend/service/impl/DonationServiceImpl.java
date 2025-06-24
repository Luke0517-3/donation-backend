package com.irent.donation_backend.service.impl;

import com.irent.donation_backend.model.Customer;
import com.irent.donation_backend.model.NGOEnvItem;
import com.irent.donation_backend.service.DonationService;
import com.irent.donation_backend.service.LarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationServiceImpl implements DonationService {

    private final LarkService larkService;

    @Override
    public Customer queryCustomer(String name) {
        larkService.getTenantAccessToken()
                .subscribe(
                        token -> System.out.println("Tenant Access Token: " + token),
                        error -> System.err.println("Error: " + error.getMessage())
                );

        return Customer.builder()
                .name(name)
                .amount(BigDecimal.valueOf(500))
                .build();
    }

    @Override
    public Mono<List<NGOEnvItem>> test() {
        return larkService.listLarkBaseNGOEnv();
    }
}
