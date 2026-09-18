package com.scheduler.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.scheduler.common.security.UserHeaders.ROLE_DELIMITER;
import static com.scheduler.common.security.UserHeaders.USER_NAME;
import static com.scheduler.common.security.UserHeaders.USER_ROLES;

/**
 * Gateway 가 넣어준 {@link UserHeaders} 를 읽어 SecurityContext 에 인증 정보를 채운다.
 * 헤더가 없으면 익명으로 통과시키고, 인가 판단은 각 서비스의 SecurityConfig 에 맡긴다.
 */
@Slf4j
public class HeaderAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String username = request.getHeader(USER_NAME);

        if (!StringUtils.hasText(username)) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(
                createAuthentication(username, request.getHeader(USER_ROLES))
        );

        filterChain.doFilter(request, response);
    }

    private Authentication createAuthentication(String username, String roles) {
        List<GrantedAuthority> authorities = parseRoles(roles);

        // 기존 JWT 기반 인증과 동일한 principal 형태를 유지해 authentication.getName() 호환
        UserDetails principal = new User(username, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    private List<GrantedAuthority> parseRoles(String roles) {
        if (!StringUtils.hasText(roles)) {
            return List.of();
        }
        return Arrays.stream(roles.split(ROLE_DELIMITER))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .<GrantedAuthority>map(SimpleGrantedAuthority::new)
                .toList();
    }
}
