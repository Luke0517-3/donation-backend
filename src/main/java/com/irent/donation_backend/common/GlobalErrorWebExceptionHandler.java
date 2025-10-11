package com.irent.donation_backend.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.irent.donation_backend.model.utils.RespBodyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.irent.donation_backend.model.utils.RespBodyDTO.getErrRespBodyDTO;

@Component
@Slf4j
@Order(-2) // 確保優先級高於默認處理器
@RequiredArgsConstructor
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("未預期的異常:", ex);

        String errMsg = ex.getMessage();

        if (log.isDebugEnabled()) {
            log.error(ExceptionUtils.getStackTrace(ex));
        }

        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            RespBodyDTO errorResponse = getErrRespBodyDTO(ex.getClass().getSimpleName(), errMsg);
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (Exception e) {
            log.error("Error serializing error response", e);
            return exchange.getResponse().setComplete();
        }
    }
}