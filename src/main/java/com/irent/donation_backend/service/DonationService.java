package com.irent.donation_backend.service;

import com.irent.donation_backend.model.Customer;
import com.irent.donation_backend.model.NGOEnvFields;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DonationService {

    Customer queryCustomer(String name);

    Mono<List<NGOEnvFields>> test();
}
