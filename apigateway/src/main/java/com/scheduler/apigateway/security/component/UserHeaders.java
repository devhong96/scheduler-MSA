package com.scheduler.apigateway.security.component;

/**
 * JWT 검증 후 내부 서비스로 전달하는 사용자 식별 헤더.
 * libs/common-security 의 UserHeaders 와 값이 같아야 한다 (Gateway 는 WebFlux 라 서블릿 기반 모듈을 공유하지 않음).
 */
public final class UserHeaders {

    public static final String USER_NAME = "X-User-Name";
    public static final String USER_ROLES = "X-User-Roles";

    private UserHeaders() {
    }
}
