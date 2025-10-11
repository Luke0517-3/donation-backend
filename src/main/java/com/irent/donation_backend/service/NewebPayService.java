package com.irent.donation_backend.service;

import com.irent.donation_backend.model.newebpay.NewebPayReqDTO;
import com.irent.donation_backend.model.newebpay.OrderInfoDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NewebPayService {

    public Mono<NewebPayReqDTO> generateNewebPayRequest(OrderInfoDTO orderInfoDTO) {
        NewebPayReqDTO response = new NewebPayReqDTO();
        response.setMerchantID("YourMerchantID");
        response.setTradeInfo(orderInfoDTO.getItemDesc());
        response.setTradeSha("GeneratedTradeShaBasedOnTradeInfo");
        response.setVersion("1.5");
        return Mono.just(response);
    }

}
