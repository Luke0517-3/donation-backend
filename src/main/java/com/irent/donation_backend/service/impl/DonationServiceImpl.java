package com.irent.donation_backend.service.impl;

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
    public Mono<LarkResponse<Customer>> queryStoreInfo(String name) {
        return larkService.getTargetStoreInfo(name);
    }

    @Override
    public Mono<List<NGOEnvFields>> test() {
        return larkService.listLarkBaseNGOEnv()
                .map(items ->
                        items.stream()
                                .map(NGOEnvListItem::getFields)
                                .toList()
                );
    }
}
