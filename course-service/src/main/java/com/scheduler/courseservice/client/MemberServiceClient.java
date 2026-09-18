package com.scheduler.courseservice.client;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import static com.scheduler.courseservice.client.dto.FeignMemberInfo.*;

@FeignClient(
        name = "scheduler-member-service",
        path = "/feign-course-member",
        url =  "${scheduler_member_service_url:}",
        configuration = MemberFeignErrorDecoder.class
)
public interface MemberServiceClient {

    @Operation(
            summary = "교사 정보 조회",
            description = "Gateway 가 전달한 사용자 헤더로 정보 조회"
    )
    @GetMapping("teacher/info")
    TeacherInfo findTeacherInfo();

    @Operation(
            summary = "학생 수업 조회",
            description = "Gateway 가 전달한 사용자 헤더로 정보 조회"
    )
    @GetMapping("student/info")
    StudentInfo findStudentInfo();

    @Operation(
            summary = "이용자의 아이디와 역할 조회"
    )
    @GetMapping("member/info")
    MemberInfo findMemberInfo();
}