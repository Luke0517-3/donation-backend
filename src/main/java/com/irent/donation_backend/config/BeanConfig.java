package com.irent.donation_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient authWebClient(WebClient.Builder builder, LarkProperties properties) {
        return builder
                .baseUrl(properties.getAUTH_URL())
                .build();
    }

    @Bean
    public WebClient bitableWebClient(WebClient.Builder builder, LarkProperties properties) {
        return builder
                .baseUrl(properties.getBITABLE_URL())
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
