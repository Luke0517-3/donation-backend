package com.irent.donation_backend.service.impl;

import com.irent.donation_backend.common.CommonConstant;
import com.irent.donation_backend.model.*;
import com.irent.donation_backend.service.DonationService;
import com.irent.donation_backend.service.LarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DonationServiceImpl implements DonationService {

    private final LarkService larkService;

    @Override
    public Mono<NGOEnvItem> queryStoreInfo(String name) {
        return larkService.getTargetStoreInfo(name)
                .map(item ->
                    item.getData().getItems().get(0).getFields()
                );
    }

    @Override
    public Mono<String> test(NGOOrderFields orderFields) {
        return larkService.createDonationOrder(orderFields)
                .map(result -> switch(result) {
            case "success" -> CommonConstant.SUCCESS_MSG.toLowerCase();
            case "失敗" -> "操作失敗";
            case "處理中" -> "請稍後查詢";
            default -> "未知狀態: " + result;
        });
    }
}
