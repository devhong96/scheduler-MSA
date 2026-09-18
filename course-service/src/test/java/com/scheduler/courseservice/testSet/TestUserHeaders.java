package com.scheduler.courseservice.testSet;

import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static com.scheduler.common.security.UserHeaders.USER_NAME;
import static com.scheduler.common.security.UserHeaders.USER_ROLES;

/**
 * Gateway 가 JWT 검증 후 넣어주는 사용자 헤더를 MockMvc 요청에 붙인다.
 * 사용: mockMvc.perform(get("/teacher/class").with(asTeacher("test_teacher")))
 */
public final class TestUserHeaders {

    private TestUserHeaders() {
    }

    public static RequestPostProcessor asAdmin(String username) {
        return asUser(username, "ADMIN");
    }

    public static RequestPostProcessor asTeacher(String username) {
        return asUser(username, "TEACHER");
    }

    public static RequestPostProcessor asStudent(String username) {
        return asUser(username, "STUDENT");
    }

    public static RequestPostProcessor asUser(String username, String roles) {
        return request -> {
            request.addHeader(USER_NAME, username);
            request.addHeader(USER_ROLES, roles);
            return request;
        };
    }
}
