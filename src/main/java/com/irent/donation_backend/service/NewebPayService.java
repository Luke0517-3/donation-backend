package com.irent.donation_backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.irent.donation_backend.common.Constants;
import com.irent.donation_backend.config.NewebPayProperties;
import com.irent.donation_backend.model.newebpay.*;
import com.irent.donation_backend.utils.AESUtils;
import com.irent.donation_backend.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewebPayService {

    private final NewebPayProperties newebPayProperties;
    private final AESUtils aesUtils;
    private final JsonUtils jsonUtils;

    public Mono<NewebPayReqDTO> generateNewebPayRequest(OrderInfoDTO orderInfoDTO) {
        try {
            TradeInfoData tradeInfoData = buildTradeInfoData(orderInfoDTO);
            String encryptTradeInfoData = aesUtils.encryptPay2go(newebPayProperties.getHASH_KEY(), newebPayProperties.getHASH_IV(), tradeInfoData.generateTradeInfoString());

            return Mono.just(NewebPayReqDTO.builder()
                    .merchantID(newebPayProperties.getMERCHANT_ID())
                    .tradeInfo(encryptTradeInfoData)
                    .tradeSha(aesUtils.sha256(newebPayProperties.getHASH_KEY(), newebPayProperties.getHASH_IV(), encryptTradeInfoData))
                    .version(newebPayProperties.getVERSION())
                    .orderId(orderInfoDTO.getOrderId())
                    .build());
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private TradeInfoData buildTradeInfoData(OrderInfoDTO orderInfoDTO) {
        return TradeInfoData.builder()
                .merchantID(newebPayProperties.getMERCHANT_ID())
                .respondType("JSON")
                .timeStamp(String.valueOf(System.currentTimeMillis() / 1000))
                .version(newebPayProperties.getVERSION())
                .langType(orderInfoDTO.getLangType())
                .merchantOrderNo(orderInfoDTO.getOrderId())
                .amt(orderInfoDTO.getAmount())
                .itemDesc(orderInfoDTO.getItemDesc())
                .tradeLimit(newebPayProperties.getTRADE_LIMIT())
                .returnURL(newebPayProperties.getRETURN_URL())
                .notifyURL(newebPayProperties.getNOTIFY_URL() + "/" + orderInfoDTO.getOrderId())
                .clientBackURL(newebPayProperties.getCLIENT_BACK_URL())
                .email(orderInfoDTO.getEmail())
                .instFlag("1")
                .build();
    }

    public Mono<String> handleNewebPayResult(NewebPayNotifyReqDTO newebPayNotifyReqDTO) {
        try {

            if (!verifyTradeSha(newebPayProperties.getHASH_KEY(), newebPayProperties.getHASH_IV(), newebPayNotifyReqDTO.getTradeInfo(), newebPayNotifyReqDTO.getTradeSha())) {
                return Mono.error(new IllegalArgumentException("TradeSha 驗證失敗"));
            }

            String decryptTradeInfo = aesUtils.decryptPay2go(newebPayProperties.getHASH_KEY(), newebPayProperties.getHASH_IV(), newebPayNotifyReqDTO.getTradeInfo());
            NewebPayNotifyResData newebPayNotifyResData = jsonUtils.parseJson(decryptTradeInfo, new TypeReference<>() {});
            log.info("NewebPay 交易結果: {}", newebPayNotifyResData);
            processPaymentResult(newebPayNotifyResData);

            return Mono.just(Constants.SUCCESS_MSG);
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    /**
     * 驗證TradeSha (SHA256雜湊值)
     */
    private boolean verifyTradeSha(String key, String iv, String tradeInfo, String tradeSha) {
        try {
            String calculatedTradeSha = aesUtils.sha256(key, iv, tradeInfo);
            return calculatedTradeSha.equalsIgnoreCase(tradeSha);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 處理支付結果
     */
    public void processPaymentResult(NewebPayNotifyResData resData) {
        String orderNo = resData.getResult().getMerchantOrderNo();
        log.info("Processing payment result for order: {}", orderNo);

        if (Constants.SUCCESS.equals(resData.getStatus())) {
            // TODO: 更新訂單狀態為已付款

        } else {
            // TODO: 更新訂單狀態為付款失敗

        }
    }
    
}
