package org.zakariafarih.recipemakerbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(EdamamConfig edamamConfig) {
        return WebClient.builder()
                .baseUrl(edamamConfig.getBaseUrl())
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
