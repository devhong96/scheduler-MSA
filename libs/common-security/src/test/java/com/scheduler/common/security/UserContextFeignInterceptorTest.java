package com.scheduler.common.security;

import feign.RequestTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.scheduler.common.security.UserHeaders.USER_NAME;
import static com.scheduler.common.security.UserHeaders.USER_ROLES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

class UserContextFeignInterceptorTest {

    private final UserContextFeignInterceptor interceptor = new UserContextFeignInterceptor();

    @AfterEach
    void clear() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    @DisplayName("인터셉터 : 현재 요청의 사용자 헤더와 Authorization 을 Feign 요청에 전파한다")
    void propagateHeaders() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(USER_NAME, "test_teacher");
        request.addHeader(USER_ROLES, "TEACHER");
        request.addHeader(AUTHORIZATION, "Bearer token");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        RequestTemplate template = new RequestTemplate();
        interceptor.apply(template);

        assertThat(template.headers().get(USER_NAME)).containsExactly("test_teacher");
        assertThat(template.headers().get(USER_ROLES)).containsExactly("TEACHER");
        assertThat(template.headers().get(AUTHORIZATION)).containsExactly("Bearer token");
    }

    @Test
    @DisplayName("인터셉터 : 요청 컨텍스트가 없으면 아무것도 추가하지 않는다")
    void skipWithoutRequestContext() {
        RequestTemplate template = new RequestTemplate();

        interceptor.apply(template);

        assertThat(template.headers()).isEmpty();
    }

    @Test
    @DisplayName("인터셉터 : 이미 지정된 헤더는 덮어쓰지 않는다")
    void doNotOverrideExistingHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(USER_NAME, "from_request");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        RequestTemplate template = new RequestTemplate().header(USER_NAME, "explicit");
        interceptor.apply(template);

        assertThat(template.headers().get(USER_NAME)).containsExactly("explicit");
    }
}
