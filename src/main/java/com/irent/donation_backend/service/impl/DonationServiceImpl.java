package com.irent.donation_backend.service.impl;

import com.irent.donation_backend.common.Constants;
import com.irent.donation_backend.model.*;
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
    public Mono<String> test(NGOOrderFields orderFields) {
        return larkService.createDonationOrder(orderFields)
                .map(result -> switch(result) {
            case Constants.SUCCESS_MSG -> result;
            case "失敗" -> "操作失敗";
            case "處理中" -> "請稍後查詢";
            default -> "未知狀態: " + result;
        });
    }
}
