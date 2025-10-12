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
    private String authUrl;
    private String appId;
    private String appSecret;
    private String appToken;
    private String tableId;
    private String bitableUrl;
    private String orderTableId;
}
