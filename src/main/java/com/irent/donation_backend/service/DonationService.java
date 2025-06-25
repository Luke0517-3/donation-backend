package com.irent.donation_backend.service;

import com.irent.donation_backend.model.Customer;
import com.irent.donation_backend.model.LarkResponse;
import com.irent.donation_backend.model.NGOEnvFields;
import com.irent.donation_backend.model.NGOEnvItem;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

public interface DonationService {

    Mono<NGOEnvItem> queryStoreInfo(String name);

    Mono<List<NGOEnvFields>> test();
}
