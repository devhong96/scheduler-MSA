package com.scheduler.memberservice.infra.security.jwt.component;

import com.scheduler.memberservice.infra.exception.custom.TokenException;
import com.scheduler.memberservice.infra.security.jwt.RefreshTokenJpaRepository;
import com.scheduler.memberservice.infra.security.jwt.domain.RefreshToken;
import com.scheduler.memberservice.infra.security.jwt.dto.JwtTokenDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.scheduler.memberservice.infra.exception.ErrorCode.NO_AUTHORITY;
import static com.scheduler.memberservice.infra.exception.ErrorCode.REFRESH_EXPIRED;
import static com.scheduler.memberservice.infra.security.jwt.filter.CreateCookie.createCookie;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final String REFRESH_CATEGORY = "refresh";

    private final JwtUtils jwtUtils;
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    /**
     * refresh 토큰으로 access 토큰을 재발급한다.
     * 재발급할 때마다 refresh 토큰도 함께 회전(rotate)시켜 새 쿠키로 내려준다.
     * 실패는 TokenException 으로 던져 GlobalException 이 상태코드를 매핑하도록 한다.
     */
    @Transactional
    public String reissue(String refresh, HttpServletResponse response) {

        if (refresh == null || refresh.isBlank()) {
            throw new TokenException(REFRESH_EXPIRED);
        }

        try {
            jwtUtils.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            refreshTokenJpaRepository.deleteByRefreshToken(refresh);
            throw new TokenException(REFRESH_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("유효하지 않은 refresh 토큰: {}", e.getMessage());
            throw new TokenException(NO_AUTHORITY);
        }

        if (!REFRESH_CATEGORY.equals(jwtUtils.getCategory(refresh))) {
            throw new TokenException(NO_AUTHORITY);
        }

        if (!refreshTokenJpaRepository.existsByRefreshToken(refresh)) {
            throw new TokenException(NO_AUTHORITY);
        }

        Authentication authentication = jwtUtils.getAuthentication(refresh);
        JwtTokenDto jwtTokenDto = jwtUtils.generateToken(authentication);

        String newRefreshToken = jwtTokenDto.getRefreshToken();
        Date expiresDate = jwtTokenDto.getExpiresDate();

        // 회전: 옛 토큰 폐기 -> 새 토큰 저장 -> 새 토큰을 쿠키로 전달
        refreshTokenJpaRepository.deleteByRefreshToken(refresh);
        refreshTokenJpaRepository.save(
                new RefreshToken(authentication.getName(), newRefreshToken, expiresDate));

        response.addCookie(createCookie(newRefreshToken));
        response.setHeader(AUTHORIZATION, jwtTokenDto.getAccessToken());

        return jwtTokenDto.getAccessToken();
    }
}
