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
    private final LarkService larkService;

    public Mono<NewebPayReqDTO> generateNewebPayRequest(OrderInfoDTO orderInfoDTO) {
        try {
            String tradeInfo = aesUtils.encryptPay2go(
                    newebPayProperties.getHASH_KEY(),
                    newebPayProperties.getHASH_IV(),
                    buildTradeInfoData(orderInfoDTO).generateTradeInfoString()
            );

            return Mono.just(NewebPayReqDTO.builder()
                    .merchantID(newebPayProperties.getMERCHANT_ID())
                    .tradeInfo(tradeInfo)
                    .tradeSha(aesUtils.sha256(newebPayProperties.getHASH_KEY(), newebPayProperties.getHASH_IV(), tradeInfo))
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
                .returnURL(newebPayProperties.getRETURN_URL() + "/" + orderInfoDTO.getCustomerName())
                .notifyURL(newebPayProperties.getNOTIFY_URL())
                .clientBackURL(newebPayProperties.getCLIENT_BACK_URL() + "/" + orderInfoDTO.getCustomerName())
                .email(orderInfoDTO.getEmail())
                .instFlag("1")
                .build();
    }

    public Mono<String> handleNewebPayResult(NewebPayNotifyReqDTO newebPayNotifyReqDTO) {
        try {
            if (!verifyTradeSha(newebPayNotifyReqDTO)) {
                return Mono.error(new IllegalArgumentException("TradeSha 驗證失敗"));
            }

            NewebPayNotifyResData resData = decryptTradeInfo(newebPayNotifyReqDTO);
            log.info("NewebPay 交易結果: {}", resData);

            return processPaymentResult(resData).thenReturn(Constants.SUCCESS_MSG);
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    /**
     * 驗證TradeSha (SHA256雜湊值)
     */
    private boolean verifyTradeSha(NewebPayNotifyReqDTO reqDTO) {
        try {
            String calculatedSha = aesUtils.sha256(
                    newebPayProperties.getHASH_KEY(),
                    newebPayProperties.getHASH_IV(),
                    reqDTO.getTradeInfo()
            );
            return calculatedSha.equalsIgnoreCase(reqDTO.getTradeSha());
        } catch (Exception e) {
            return false;
        }
    }

    private NewebPayNotifyResData decryptTradeInfo(NewebPayNotifyReqDTO reqDTO) throws Exception {
        String decryptedInfo = aesUtils.decryptPay2go(
                newebPayProperties.getHASH_KEY(),
                newebPayProperties.getHASH_IV(),
                reqDTO.getTradeInfo()
        );
        return jsonUtils.parseJson(decryptedInfo, new TypeReference<>() {
        });
    }

    /**
     * 處理支付結果
     */
    private Mono<Void> processPaymentResult(NewebPayNotifyResData resData) {
        String orderNo = resData.getResult().getMerchantOrderNo();
        log.info("Processing payment result for order: {}", orderNo);

        int status = Constants.SUCCESS.equals(resData.getStatus()) ? 1 : 2;
        return larkService.updateDonationOrder(orderNo, status)
                .doOnSuccess(result -> log.info("Order {} updated to status {}.", orderNo, status))
                .doOnError(error -> log.error("Failed to update order {}: {}", orderNo, error.getMessage()))
                .then();
    }

}
