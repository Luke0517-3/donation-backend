package com.irent.donation_backend.service.impl;

import com.irent.donation_backend.model.Customer;
import com.irent.donation_backend.service.DonationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DonationServiceImpl implements DonationService {

    @Override
    public Customer queryCustomer(String name) {
        return Customer.builder()
                .name(name)
                .amount(BigDecimal.valueOf(500))
                .build();
    }
}
