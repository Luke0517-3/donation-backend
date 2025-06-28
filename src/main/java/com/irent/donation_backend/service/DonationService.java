package com.irent.donation_backend.service;

import com.irent.donation_backend.model.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

public interface DonationService {

    Mono<NGOEnvItem> queryStoreInfo(String name);

    Mono<String> test(NGOOrderFields orderFields);
}
