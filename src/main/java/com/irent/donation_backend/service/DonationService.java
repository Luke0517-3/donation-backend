package com.irent.donation_backend.service;

import com.irent.donation_backend.model.lark.NGOEnvItem;
import com.irent.donation_backend.model.lark.NGOOrderFields;
import com.irent.donation_backend.model.newebpay.OrderInfoDTO;
import reactor.core.publisher.Mono;

public interface DonationService {

    Mono<NGOEnvItem> queryStoreInfo(String name);

    Mono<OrderInfoDTO> createOrder(NGOOrderFields orderFields);

    Mono<String> updateOrder(String recordId, Integer payStatus);
}
