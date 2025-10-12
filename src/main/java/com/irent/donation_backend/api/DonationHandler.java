package com.irent.donation_backend.api;

import com.irent.donation_backend.model.lark.NGOOrderFields;
import com.irent.donation_backend.model.newebpay.NewebPayNotifyReqDTO;
import com.irent.donation_backend.model.newebpay.OrderInfoDTO;
import com.irent.donation_backend.model.utils.RespBodyDTO;
import com.irent.donation_backend.service.DonationService;
import com.irent.donation_backend.service.NewebPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class DonationHandler {

    private final DonationService donationService;
    private final NewebPayService newebPayService;

    /**
     * 創建成功回應，使用RespBodyDTO包裝
     */
    private <T> Mono<ServerResponse> createSuccessResponse(String type, T data) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(RespBodyDTO.getRespBodyDTO(type, data)), RespBodyDTO.class);
    }

    /**
     * 統一錯誤處理，使用RespBodyDTO包裝
     */
    private Mono<ServerResponse> handleError(String type, Throwable ex) {
        log.error("處理請求時發生錯誤", ex);
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(RespBodyDTO.getErrRespBodyDTO(type, ex.getMessage())), RespBodyDTO.class);
    }

    /**
     * 取得捐款目標機構資訊
     */
    public Mono<ServerResponse> getCustomer(ServerRequest request) {
        return Mono.just(request.pathVariable("name"))
                .doOnNext(name -> log.info("查詢機構資訊: {}", name))
                .flatMap(donationService::queryStoreInfo)
                .flatMap(fields -> createSuccessResponse("queryStoreInfo", fields))
                .switchIfEmpty(ServerResponse.noContent().build())
                .onErrorResume(ex -> handleError("queryStoreInfo", ex));
    }


    /**
     * 建立捐款訂單並取得藍星支付請求資訊
     */
    public Mono<ServerResponse> createOrder(ServerRequest request) {
        return request.bodyToMono(NGOOrderFields.class)
                .doOnNext(order -> log.info("創建捐款訂單: {}", order))
                .flatMap(donationService::createOrder)
                .doOnNext(orderInfo -> log.info("訂單已創建完成: {}", orderInfo))
                .flatMap(newebPayService::generateNewebPayRequest)
                .flatMap(result -> createSuccessResponse("createOrder", result))
                .switchIfEmpty(ServerResponse.noContent().build())
                .onErrorResume(ex -> handleError("createOrder", ex));
    }

    /**
     * 取得藍星支付請求資訊
     */
    public Mono<ServerResponse> retrieveNewebPayRequest(ServerRequest request) {
        return request.bodyToMono(OrderInfoDTO.class)
                .doOnNext(orderInfo -> log.info("生成支付請求: {}", orderInfo))
                .flatMap(newebPayService::generateNewebPayRequest)
                .flatMap(result -> createSuccessResponse("retrieveNewebPayRequest", result))
                .switchIfEmpty(ServerResponse.noContent().build())
                .onErrorResume(ex -> handleError("retrieveNewebPayRequest", ex));
    }

    /**
     * 接收藍星付款結果資訊
     */
    public Mono<ServerResponse> handleNewebPayNotify(ServerRequest request) {
        return request.bodyToMono(NewebPayNotifyReqDTO.class)
                .doOnNext(notifyReq -> log.info("接收藍星支付通知: {}", notifyReq))
                .flatMap(newebPayService::handleNewebPayResult)
                .flatMap(result -> createSuccessResponse("handleNewebPayNotify", result))
                .switchIfEmpty(ServerResponse.noContent().build())
                .onErrorResume(ex -> handleError("handleNewebPayNotify", ex));
    }

    /**
     * 查詢訂單資訊
     */
    public Mono<ServerResponse> queryOrderInfo(ServerRequest request) {
        return Mono.just(request.pathVariable("recordId"))
                .doOnNext(recordId -> log.info("查詢訂單資訊: {}", recordId))
                .flatMap(donationService::queryOrderInfo)
                .flatMap(fields -> createSuccessResponse("queryStoreInfo", fields))
                .switchIfEmpty(ServerResponse.noContent().build())
                .onErrorResume(ex -> handleError("queryStoreInfo", ex));
    }
}