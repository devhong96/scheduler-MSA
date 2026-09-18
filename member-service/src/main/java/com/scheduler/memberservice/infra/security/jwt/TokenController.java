package com.scheduler.memberservice.infra.security.jwt;

import com.scheduler.memberservice.infra.exception.custom.TokenException;
import com.scheduler.memberservice.infra.security.jwt.component.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.scheduler.memberservice.infra.exception.ErrorCode.TOKEN_EXPIRED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "토큰 만료 알림", description = "만료된 액세스 토큰으로 접근했음을 알린다")
    @PostMapping("expired")
    public ResponseEntity<String> auth() {
        throw new TokenException(TOKEN_EXPIRED);
    }

    @Operation(summary = "토큰 재발급", description = "refresh 쿠키로 액세스 토큰을 재발급하고 refresh 토큰을 회전시킨다")
    @PostMapping("reissue")
    public ResponseEntity<Map<String, String>> refreshAuth(
            @CookieValue(value = "refresh", required = false) String refresh,
            HttpServletResponse response
    ) {
        return new ResponseEntity<>(
                Map.of("access", refreshTokenService.reissue(refresh, response)), OK);
    }
}
