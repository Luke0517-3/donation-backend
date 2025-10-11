package com.irent.donation_backend.service;

import com.irent.donation_backend.model.lark.Customer;
import com.irent.donation_backend.model.lark.NGOEnvListItem;
import com.irent.donation_backend.model.lark.NGOOrderFields;
import reactor.core.publisher.Mono;

import java.util.List;

public interface LarkService {
    Mono<String> getTenantAccessToken();
    Mono<List<NGOEnvListItem>> listLarkBaseNGOEnv();

    Mono<Customer> getTargetStoreInfo(String path);

    Mono<String> createDonationOrder(NGOOrderFields orderFields);
}

