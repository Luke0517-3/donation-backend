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
        logger.info("MERCHANT_ID: {}", newebPayProperties.getMERCHANT_ID());
        logger.info("RETURN_URL: {}", newebPayProperties.getRETURN_URL());
        logger.info("RETURN_FRONTEND: {}", newebPayProperties.getRETURN_FRONTEND());
        logger.info("RETURN_FRONTEND_FAIL: {}", newebPayProperties.getRETURN_FRONTEND_FAIL());
        logger.info("NOTIFY_URL: {}", newebPayProperties.getNOTIFY_URL());
        logger.info("CLIENT_BACK_URL: {}", newebPayProperties.getCLIENT_BACK_URL());
        logger.info("VERSION: {}", newebPayProperties.getVERSION());
    }

}
