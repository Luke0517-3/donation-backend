package com.irent.donation_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.logging.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Configuration
@Slf4j
public class BeanConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient authWebClient(WebClient.Builder builder, LarkProperties properties) {
        return builder
                .baseUrl(properties.getAUTH_URL())
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .build();
    }

    @Bean
    public WebClient bitableWebClient(WebClient.Builder builder, LarkProperties properties) {
        return builder
                .baseUrl(properties.getBITABLE_URL())
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .build();
    }

    /**
     * To log request/response detail
     *
     * @return
     */
    @Bean
    public HttpClient httpClient() {
        return HttpClient
                .create()
                .wiretap("reactor.netty.http.client.HttpClient",
                        LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
