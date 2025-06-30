package com.irent.donation_backend.service;

import com.irent.donation_backend.model.Customer;
import com.irent.donation_backend.model.LarkResponse;
import com.irent.donation_backend.model.NGOEnvListItem;
import com.irent.donation_backend.model.NGOOrderFields;
import reactor.core.publisher.Mono;

import java.util.List;

public interface LarkService {
    Mono<String> getTenantAccessToken();
    Mono<List<NGOEnvListItem>> listLarkBaseNGOEnv();

    Mono<Customer> getTargetStoreInfo(String path);

    Mono<String> createDonationOrder(NGOOrderFields orderFields);
}

