package com.irent.donation_backend.listener;

import com.irent.donation_backend.config.NewebPayProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class NewebPayConfigValidator implements ApplicationListener<ApplicationReadyEvent> {

    private final NewebPayProperties newebPayProperties;
    private final Logger logger = LoggerFactory.getLogger(NewebPayConfigValidator.class);

    public NewebPayConfigValidator(NewebPayProperties newebPayProperties) {
        this.newebPayProperties = newebPayProperties;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("=== NewebPay 配置檢查 ===");
        logger.info("MERCHANT_ID: {}", newebPayProperties.getMerchantId());
        logger.info("RETURN_URL: {}", newebPayProperties.getReturnUrl());
        logger.info("RETURN_FRONTEND: {}", newebPayProperties.getReturnFrontend());
        logger.info("RETURN_FRONTEND_FAIL: {}", newebPayProperties.getReturnFrontendFail());
        logger.info("NOTIFY_URL: {}", newebPayProperties.getNotifyUrl());
        logger.info("CLIENT_BACK_URL: {}", newebPayProperties.getClientBackUrl());
        logger.info("VERSION: {}", newebPayProperties.getVersion());
    }

}
