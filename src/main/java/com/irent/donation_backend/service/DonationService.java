package com.irent.donation_backend.service;

import com.irent.donation_backend.model.lark.NGOEnvItem;
import com.irent.donation_backend.model.lark.NGOOrderFields;
import reactor.core.publisher.Mono;

public interface DonationService {

    Mono<NGOEnvItem> queryStoreInfo(String name);

    Mono<String> createOrder(NGOOrderFields orderFields);
}
