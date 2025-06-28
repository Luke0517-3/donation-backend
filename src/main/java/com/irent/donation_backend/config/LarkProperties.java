package com.irent.donation_backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "lark", ignoreInvalidFields = true)
public class LarkProperties {
    private String AUTH_URL;
    private String APP_ID;
    private String APP_SECRET;
    private String APP_TOKEN;
    private String TABLE_ID;
    private String BITABLE_URL;
    private String ORDER_TABLE_ID;
}
