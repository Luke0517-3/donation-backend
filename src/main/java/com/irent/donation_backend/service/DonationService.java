package com.irent.donation_backend.service;

import com.irent.donation_backend.model.Customer;

public interface DonationService {

    Customer queryCustomer (String name);
}
