package com.scheduler.common.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.scheduler.common.security.UserHeaders.USER_NAME;
import static com.scheduler.common.security.UserHeaders.USER_ROLES;
import static org.assertj.core.api.Assertions.assertThat;

class HeaderAuthFilterTest {

    private final HeaderAuthFilter filter = new HeaderAuthFilter();

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("필터 : 사용자 헤더가 있으면 인증 정보를 채운다")
    void authenticateFromHeaders() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(USER_NAME, "test_teacher");
        request.addHeader(USER_ROLES, "TEACHER, ADMIN");

        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("test_teacher");
        assertThat(authentication.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("TEACHER", "ADMIN");
    }

    @Test
    @DisplayName("필터 : 사용자 헤더가 없으면 익명으로 통과한다")
    void passThroughWithoutHeaders() throws Exception {
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(), chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        assertThat(chain.getRequest()).isNotNull();
    }

    @Test
    @DisplayName("필터 : 권한 헤더가 비어 있으면 권한 없는 인증으로 처리한다")
    void authenticateWithoutRoles() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(USER_NAME, "test_student");

        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication.getName()).isEqualTo("test_student");
        assertThat(authentication.getAuthorities()).isEmpty();
    }
}
