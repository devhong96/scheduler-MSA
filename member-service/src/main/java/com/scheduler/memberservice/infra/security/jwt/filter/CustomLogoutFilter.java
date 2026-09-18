package com.scheduler.memberservice.infra.security.jwt.filter;

import com.scheduler.memberservice.infra.security.jwt.RefreshTokenJpaRepository;
import com.scheduler.memberservice.infra.security.jwt.component.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String REFRESH_CATEGORY = "refresh";

    private final JwtUtils jwtUtils;
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        if (!request.getRequestURI().matches("^/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(AUTHORIZATION);

        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            response.setStatus(SC_BAD_REQUEST);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        String refreshToken = header.substring(BEARER_PREFIX.length()).trim();

        // 만료된 토큰이어도 저장소에서는 지워야 하므로 파싱 예외를 먼저 처리한다.
        String category;
        try {
            category = jwtUtils.getCategory(refreshToken);
        } catch (ExpiredJwtException e) {
            refreshTokenJpaRepository.deleteByRefreshToken(refreshToken);
            response.setStatus(SC_OK);
            return;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("로그아웃 요청의 토큰이 유효하지 않습니다: {}", e.getMessage());
            response.setStatus(SC_BAD_REQUEST);
            response.getWriter().write("Invalid refresh token");
            return;
        }

        if (!REFRESH_CATEGORY.equals(category)) {
            response.setStatus(SC_BAD_REQUEST);
            response.getWriter().write("not refreshToken or category is null");
            return;
        }

        if (!refreshTokenJpaRepository.existsByRefreshToken(refreshToken)) {
            response.setStatus(SC_BAD_REQUEST);
            return;
        }

        refreshTokenJpaRepository.deleteByRefreshToken(refreshToken);
        response.setStatus(SC_OK);
    }
}
