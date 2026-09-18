package com.scheduler.memberservice.testSet;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.stream.Collectors;

import static com.scheduler.common.security.UserHeaders.ROLE_DELIMITER;
import static com.scheduler.common.security.UserHeaders.USER_NAME;
import static com.scheduler.common.security.UserHeaders.USER_ROLES;

/**
 * Gateway 가 JWT 검증 후 넣어주는 사용자 헤더를 MockMvc 요청에 붙인다.
 * 사용: @WithTeacher(...) 로 만든 시큐리티 컨텍스트를 그대로 헤더로 옮긴다.
 *   mockMvc.perform(get("/teacher/manage/info").with(userHeaders()))
 */
public final class TestUserHeaders {

    private TestUserHeaders() {
    }

    public static RequestPostProcessor userHeaders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // JwtUtils.generateToken 과 같은 규칙: ROLE_ 접두어를 뗀 권한을 콤마로 연결
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> authority.replaceFirst("^ROLE_", ""))
                .collect(Collectors.joining(ROLE_DELIMITER));

        return request -> {
            request.addHeader(USER_NAME, authentication.getName());
            request.addHeader(USER_ROLES, roles);
            return request;
        };
    }
}
