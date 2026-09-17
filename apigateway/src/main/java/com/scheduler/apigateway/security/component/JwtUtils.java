package com.scheduler.apigateway.security.component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import static io.jsonwebtoken.io.Decoders.BASE64;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private SecretKey signingKey;

    @PostConstruct
    public void createSigningKey() {
        byte[] keyBytes = BASE64.decode(secretKey);
        signingKey =  Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims validateAndGetClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new JwtException("Expired JWT token.");
        } catch (SecurityException | MalformedJwtException e) {
            throw new JwtException("Invalid JWT signature.");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("Invalid token signature.");
        } catch (IllegalArgumentException e) {
            throw new JwtException("Invalid JWT token.");
        }
    }
}
