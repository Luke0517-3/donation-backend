package com.irent.donation_backend.service;

import com.irent.donation_backend.model.NGOEnvItem;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface LarkService {
    Mono<String> getTenantAccessToken();
    Mono<List<NGOEnvItem>> listLarkBaseNGOEnv();
}

