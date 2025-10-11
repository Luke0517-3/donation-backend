package com.irent.donation_backend.service.impl;

import com.irent.donation_backend.model.lark.Customer;
import com.irent.donation_backend.model.lark.NGOEnvItem;
import com.irent.donation_backend.model.lark.NGOOrderFields;
import com.irent.donation_backend.model.newebpay.OrderInfoDTO;
import com.irent.donation_backend.service.DonationService;
import com.irent.donation_backend.service.LarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DonationServiceImpl implements DonationService {

    private final LarkService larkService;

    @Override
    public Mono<NGOEnvItem> queryStoreInfo(String name) {
        return larkService.getTargetStoreInfo(name)
                .map(Customer::getFields);
    }

    @Override
    public Mono<OrderInfoDTO> createOrder(NGOOrderFields orderFields) {
        return larkService.createDonationOrder(orderFields);
    }

    @Override
    public Mono<String> updateOrder(String recordId, Integer payStatus) {
        return larkService.updateDonationOrder(recordId, payStatus);
    }
}
