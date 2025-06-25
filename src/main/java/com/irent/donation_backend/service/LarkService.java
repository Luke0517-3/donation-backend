package com.irent.donation_backend.service;

import com.irent.donation_backend.model.Customer;
import com.irent.donation_backend.model.LarkResponse;
import com.irent.donation_backend.model.NGOEnvItem;
import com.irent.donation_backend.model.NGOEnvListItem;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

public interface LarkService {
    Mono<String> getTenantAccessToken();
    Mono<List<NGOEnvListItem>> listLarkBaseNGOEnv();

    Mono<LarkResponse<Customer>> getTargetStoreInfo(String path);
}

