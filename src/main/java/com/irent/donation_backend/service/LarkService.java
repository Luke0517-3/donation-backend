package com.irent.donation_backend.service;

import reactor.core.publisher.Mono;

public interface LarkService {
    Mono<String> getTenantAccessToken();
}
