package com.scheduler.common.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static com.scheduler.common.security.UserHeaders.USER_NAME;
import static com.scheduler.common.security.UserHeaders.USER_ROLES;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * 현재 요청의 사용자 식별 헤더를 Feign 호출에 그대로 전파한다.
 * Authorization 도 함께 넘겨 아직 토큰 기반으로 동작하는 서비스와의 호환을 유지한다.
 */
public class UserContextFeignInterceptor implements RequestInterceptor {

    private static final List<String> PROPAGATED_HEADERS = List.of(USER_NAME, USER_ROLES, AUTHORIZATION);

    @Override
    public void apply(RequestTemplate template) {
        HttpServletRequest request = currentRequest();
        if (request == null) {
            return;
        }
        for (String header : PROPAGATED_HEADERS) {
            String value = request.getHeader(header);
            if (value != null && !template.headers().containsKey(header)) {
                template.header(header, value);
            }
        }
    }

    private HttpServletRequest currentRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest();
        }
        return null;
    }
}
