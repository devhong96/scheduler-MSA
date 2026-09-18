package com.scheduler.common.security;

/**
 * API Gateway 가 JWT 검증 후 내부 서비스로 전달하는 사용자 식별 헤더.
 * 내부 서비스는 토큰을 직접 파싱하지 않고 이 헤더만 신뢰한다.
 * (내부 서비스는 Gateway 를 거치지 않고는 외부에서 접근할 수 없어야 한다)
 */
public final class UserHeaders {

    /** 사용자 계정 이름 (JWT subject) */
    public static final String USER_NAME = "X-User-Name";

    /** 권한 목록, 콤마 구분 (예: "TEACHER" / "ADMIN,TEACHER") */
    public static final String USER_ROLES = "X-User-Roles";

    public static final String ROLE_DELIMITER = ",";

    private UserHeaders() {
    }
}
