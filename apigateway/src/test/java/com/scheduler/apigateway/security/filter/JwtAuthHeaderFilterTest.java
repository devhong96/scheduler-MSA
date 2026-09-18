package com.scheduler.apigateway.security.filter;

import com.scheduler.apigateway.security.component.JwtUtils;
import com.scheduler.apigateway.security.component.UserHeaders;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtAuthHeaderFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private GatewayFilterChain chain;

    private JwtAuthHeaderFilter jwtAuthHeaderFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthHeaderFilter = new JwtAuthHeaderFilter(jwtUtils);
        when(chain.filter(org.mockito.ArgumentMatchers.any(ServerWebExchange.class)))
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("Valid token should pass the filter")
    void validToken_shouldPass() {
        // Given
        String token = "valid.token.here";
        MockServerHttpRequest request = MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        
        Claims claims = new DefaultClaims(Map.of("category", "access"));
        when(jwtUtils.validateAndGetClaims(token)).thenReturn(claims);

        GatewayFilter filter = jwtAuthHeaderFilter.apply(new JwtAuthHeaderFilter.Config());

        // When
        Mono<Void> result = filter.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    @DisplayName("검증된 토큰의 subject/auth 를 X-User-Name / X-User-Roles 헤더로 전달한다")
    void validToken_shouldForwardUserHeaders() {
        // Given
        String token = "valid.token.here";
        MockServerHttpRequest request = MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        Claims claims = new DefaultClaims(Map.of("sub", "test_teacher", "auth", "TEACHER", "category", "access"));
        when(jwtUtils.validateAndGetClaims(token)).thenReturn(claims);
        ArgumentCaptor<ServerWebExchange> captor = ArgumentCaptor.forClass(ServerWebExchange.class);

        GatewayFilter filter = jwtAuthHeaderFilter.apply(new JwtAuthHeaderFilter.Config());
        // When
        StepVerifier.create(filter.filter(exchange, chain)).verifyComplete();
        // Then
        verify(chain).filter(captor.capture());
        HttpHeaders forwarded = captor.getValue().getRequest().getHeaders();
        assertThat(forwarded.getFirst(UserHeaders.USER_NAME)).isEqualTo("test_teacher");
        assertThat(forwarded.getFirst(UserHeaders.USER_ROLES)).isEqualTo("TEACHER");
        assertThat(forwarded.getFirst(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer " + token);
    }

    @Test
    @DisplayName("클라이언트가 보낸 X-User-* 헤더는 토큰의 값으로 덮어쓴다")
    void spoofedUserHeaders_shouldBeReplaced() {
        // Given
        String token = "valid.token.here";
        MockServerHttpRequest request = MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header(UserHeaders.USER_NAME, "hacker")
                .header(UserHeaders.USER_ROLES, "ADMIN")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        Claims claims = new DefaultClaims(Map.of("sub", "test_student", "auth", "STUDENT", "category", "access"));
        when(jwtUtils.validateAndGetClaims(token)).thenReturn(claims);
        ArgumentCaptor<ServerWebExchange> captor = ArgumentCaptor.forClass(ServerWebExchange.class);

        GatewayFilter filter = jwtAuthHeaderFilter.apply(new JwtAuthHeaderFilter.Config());
        // When
        StepVerifier.create(filter.filter(exchange, chain)).verifyComplete();
        // Then
        verify(chain).filter(captor.capture());
        HttpHeaders forwarded = captor.getValue().getRequest().getHeaders();
        assertThat(forwarded.get(UserHeaders.USER_NAME)).containsExactly("test_student");
        assertThat(forwarded.get(UserHeaders.USER_ROLES)).containsExactly("STUDENT");
    }

    @Test
    @DisplayName("Missing Authorization header should return UNAUTHORIZED")
    void missingHeader_shouldReturnUnauthorized() {
        // Given
        MockServerHttpRequest request = MockServerHttpRequest.get("/").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        
        GatewayFilter filter = jwtAuthHeaderFilter.apply(new JwtAuthHeaderFilter.Config());

        // When
        Mono<Void> result = filter.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
        
        assert exchange.getResponse().getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    @Test
    @DisplayName("Invalid header format should return UNAUTHORIZED")
    void invalidHeaderFormat_shouldReturnUnauthorized() {
        // Given
        MockServerHttpRequest request = MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "InvalidFormat")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        GatewayFilter filter = jwtAuthHeaderFilter.apply(new JwtAuthHeaderFilter.Config());

        // When
        Mono<Void> result = filter.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        assert exchange.getResponse().getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    @Test
    @DisplayName("Expired or invalid token should return UNAUTHORIZED")
    void invalidToken_shouldReturnUnauthorized() {
        // Given
        String token = "invalid.token";
        MockServerHttpRequest request = MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(jwtUtils.validateAndGetClaims(token)).thenThrow(new JwtException("Invalid token"));

        GatewayFilter filter = jwtAuthHeaderFilter.apply(new JwtAuthHeaderFilter.Config());

        // When
        Mono<Void> result = filter.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        assert exchange.getResponse().getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    @Test
    @DisplayName("Invalid category should return BAD_REQUEST")
    void invalidCategory_shouldReturnBadRequest() {
        // Given
        String token = "refresh.token";
        MockServerHttpRequest request = MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        Claims claims = new DefaultClaims(Map.of("category", "refresh"));
        when(jwtUtils.validateAndGetClaims(token)).thenReturn(claims);

        GatewayFilter filter = jwtAuthHeaderFilter.apply(new JwtAuthHeaderFilter.Config());

        // When
        Mono<Void> result = filter.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        assert exchange.getResponse().getStatusCode() == HttpStatus.BAD_REQUEST;
    }
}
