package com.scheduler.apigateway.security.filter;

import com.scheduler.apigateway.security.component.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static java.nio.charset.StandardCharsets.UTF_8;
import static com.scheduler.apigateway.security.component.UserHeaders.USER_NAME;
import static com.scheduler.apigateway.security.component.UserHeaders.USER_ROLES;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
public class JwtAuthHeaderFilter extends AbstractGatewayFilterFactory<JwtAuthHeaderFilter.Config> {

    private final JwtUtils jwtUtils;

    public JwtAuthHeaderFilter(JwtUtils jwtUtils) {
        super(Config.class);
        this.jwtUtils = jwtUtils;
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = stripUserHeaders(exchange.getRequest());
            ServerHttpResponse response = exchange.getResponse();

            String accessToken = request.getHeaders().getFirst(AUTHORIZATION);

            if (accessToken == null) {
                response.setStatusCode(UNAUTHORIZED);
                return writeErrorResponse(response, UNAUTHORIZED, "Missing Authorization header");
            }

            if (!accessToken.startsWith("Bearer ")) {
                log.warn("Authorization header does not start with Bearer");
                response.setStatusCode(UNAUTHORIZED);
                return writeErrorResponse(response, UNAUTHORIZED, "Invalid Authorization header format");
            }

            accessToken = accessToken.replace("Bearer ", "").trim();

            Claims claims;
            try {
                claims = jwtUtils.validateAndGetClaims(accessToken);
            } catch (JwtException e) {
                log.warn("Token validation failed: {}", e.getMessage());
                response.setStatusCode(UNAUTHORIZED);
                return writeErrorResponse(response, UNAUTHORIZED, e.getMessage());
            }

            String category = claims.get("category", String.class);
            if (category == null || !category.equals("access")) {
                log.warn("Invalid token category: {}", category);
                response.setStatusCode(BAD_REQUEST);
                return writeErrorResponse(response, BAD_REQUEST, "Invalid category");
            }

            // 검증된 claims 를 사용자 헤더로 변환해 내부 서비스로 전달한다
            ServerHttpRequest authenticatedRequest = request.mutate()
                    .headers(headers -> {
                        headers.set(USER_NAME, claims.getSubject());
                        headers.set(USER_ROLES, claims.get("auth", String.class));
                    })
                    .build();

            return chain.filter(exchange.mutate().request(authenticatedRequest).build());
        };
    }

    // 클라이언트가 직접 보낸 사용자 헤더는 신뢰하지 않는다
    private ServerHttpRequest stripUserHeaders(ServerHttpRequest request) {
        return request.mutate()
                .headers(headers -> {
                    headers.remove(USER_NAME);
                    headers.remove(USER_ROLES);
                })
                .build();
    }

    private Mono<Void> writeErrorResponse(ServerHttpResponse response, HttpStatus status, String message) {
        response.getHeaders().setContentType(APPLICATION_JSON);
        response.setStatusCode(status);

        DataBuffer buffer = response.bufferFactory().wrap(
                ("{\"error\":\"" + message + "\"}").getBytes(UTF_8)
        );
        return response.writeWith(Mono.just(buffer));
    }
}
