package com.scheduler.articleservice.infra.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class Config {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.filter(errorHandlingFilter()).build();
    }

    private ExchangeFilterFunction errorHandlingFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            if (response.statusCode().isError()) {
                return response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("error code={}, error message={}", response.statusCode(), errorBody);
                            return Mono.error(new RuntimeException("준비 실패: " + errorBody));
                        });
            }
            return Mono.just(response); // 정상 응답은 그대로 반환
        });
    }
}
