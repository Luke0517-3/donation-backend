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
    private String URL;
    private String MERCHANT_ID;
    private String HASH_KEY;
    private String HASH_IV;
    private String PAY_GATEWAY;
    private String RETURN_URL;
    private String RETURN_FRONTEND;
    private String RETURN_FRONTEND_FAIL;
    private String CLIENT_BACK_URL;
    private String NOTIFY_URL;
    private String PAYMENT_URL;
    private String CLIENT_BACK;
    private String VERSION;
}
