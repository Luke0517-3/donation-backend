package com.irent.donation_backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "newebpay", ignoreInvalidFields = true)
public class NewebPayProperties {
    private String url;
    private String merchantId;
    private String hashKey;
    private String hashIv;
    private String payGateway;
    private String returnUrl;
    private String returnFrontend;
    private String returnFrontendFail;
    private String clientBackUrl;
    private String notifyUrl;
    private String paymentUrl;
    private String version;
    private Integer tradeLimit = 600;
}
