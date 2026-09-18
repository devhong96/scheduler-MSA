package com.scheduler.memberservice.client.controller;

import com.scheduler.memberservice.client.service.FeignCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.scheduler.memberservice.client.dto.FeignMemberResponse.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/feign-course-member")
@RequiredArgsConstructor
public class FeignCourseController {

    private final FeignCourseService feignCourseService;

    @GetMapping("/teacher/info")
    public ResponseEntity<TeacherInfo> findTeacherInfo(
            @AuthenticationPrincipal(expression = "username") String username
    ){
        return new ResponseEntity<>(feignCourseService.findTeacherInfo(username), OK);
    }

    @GetMapping("/student/info")
    public ResponseEntity<StudentInfo> findStudentInfo(
            @AuthenticationPrincipal(expression = "username") String username
    ){
        return new ResponseEntity<>(feignCourseService.findStudentInfo(username), OK);
    }

    @GetMapping("/member/info")
    public ResponseEntity<MemberInfo> findMemberInfo(Authentication authentication) {
        return new ResponseEntity<>(feignCourseService.findMemberInfo(authentication), OK);
    }
}
