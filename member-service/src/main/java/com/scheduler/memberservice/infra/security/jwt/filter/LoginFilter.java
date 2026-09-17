package com.scheduler.memberservice.infra.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.memberservice.infra.security.base.AdminDetails;
import com.scheduler.memberservice.infra.security.base.StudentDetails;
import com.scheduler.memberservice.infra.security.base.TeacherDetails;
import com.scheduler.memberservice.infra.security.jwt.RefreshTokenJpaRepository;
import com.scheduler.memberservice.infra.security.jwt.component.JwtUtils;
import com.scheduler.memberservice.infra.security.jwt.domain.RefreshToken;
import com.scheduler.memberservice.infra.security.jwt.dto.JwtTokenDto;
import com.scheduler.memberservice.infra.security.jwt.dto.UsernamePasswordAutoDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static com.scheduler.memberservice.infra.security.jwt.filter.CreateCookie.createCookie;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response
    ) throws AuthenticationException {

        UsernamePasswordAutoDto usernamePasswordAuthDto;

        try {
            usernamePasswordAuthDto = new ObjectMapper()
                    .readValue(request.getInputStream(), UsernamePasswordAutoDto.class);

        } catch (IOException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }

        String username = usernamePasswordAuthDto.getUsername();
        String password = usernamePasswordAuthDto.getPassword();

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult
    ) {

        JwtTokenDto jwtTokenDto = jwtUtils.generateToken(authResult);

        String userId = resolveUserId(authResult.getPrincipal());

        if (userId != null) {
            // 재로그인 시 기존 토큰을 폐기하고 새로 발급한 토큰으로 교체한다.
            refreshTokenJpaRepository.findRefreshTokenByUserId(userId)
                    .ifPresent(token -> refreshTokenJpaRepository.deleteByRefreshToken(token.getRefreshToken()));

            refreshTokenJpaRepository.save(new RefreshToken(
                    userId, jwtTokenDto.getRefreshToken(), jwtTokenDto.getExpiresDate()));

            response.addCookie(createCookie(jwtTokenDto.getRefreshToken()));
        }

        response.setHeader(AUTHORIZATION, jwtTokenDto.getAccessToken());
        response.setStatus(HttpStatus.OK.value());
    }

    private String resolveUserId(Object principal) {

        if (principal instanceof StudentDetails studentDetails) {
            return studentDetails.getStudent().getStudentId();
        }

        if (principal instanceof TeacherDetails teacherDetails) {
            return teacherDetails.getTeacher().getTeacherId();
        }

        if (principal instanceof AdminDetails adminDetails) {
            return adminDetails.getAdmin().getAdminId();
        }

        log.warn("알 수 없는 principal 타입: {}", principal.getClass().getName());
        return null;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException exception
    ) throws IOException {

        String errorMessage;
        if (exception instanceof UsernameNotFoundException) {
            errorMessage = "등록되지 않은 아이디입니다";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "아이디가 존재하지 않습니다. 아이디를 확인해주세요";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 또는 비밀번호가 잘못되었습니다.";
        } else if (exception instanceof DisabledException) {
            errorMessage = "관리자의 승인을 기다려주세요";
        } else {
            errorMessage = "관리자에게 문의해주세요";
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(SC_UNAUTHORIZED);
        response.getWriter().write(errorMessage);
    }


}
