package com.irent.donation_backend.service;

import com.irent.donation_backend.model.NGOEnvItem;
import com.irent.donation_backend.model.NGOOrderFields;
import reactor.core.publisher.Mono;

public interface DonationService {

    Mono<NGOEnvItem> queryStoreInfo(String name);

    Mono<String> test(NGOOrderFields orderFields);
}
