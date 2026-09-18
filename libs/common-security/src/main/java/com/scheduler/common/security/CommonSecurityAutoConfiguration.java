package com.scheduler.common.security;

import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

/**
 * 서비스가 의존성만 추가하면 헤더 인증 필터와 Feign 헤더 전파가 활성화된다.
 * 각 서비스의 SecurityConfig 에서 {@link HeaderAuthFilter} 빈을 필터 체인에 등록해 사용한다.
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = SERVLET)
public class CommonSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HeaderAuthFilter headerAuthFilter() {
        return new HeaderAuthFilter();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(RequestInterceptor.class)
    static class FeignPropagationConfiguration {

        @Bean
        @ConditionalOnMissingBean(UserContextFeignInterceptor.class)
        public UserContextFeignInterceptor userContextFeignInterceptor() {
            return new UserContextFeignInterceptor();
        }
    }
}
