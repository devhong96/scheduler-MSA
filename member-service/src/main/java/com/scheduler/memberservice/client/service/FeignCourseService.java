package com.scheduler.memberservice.client.service;

import static com.scheduler.memberservice.client.dto.FeignMemberResponse.*;

import org.springframework.security.core.Authentication;

public interface FeignCourseService {

    TeacherInfo findTeacherInfo(String username);

    StudentInfo findStudentInfo(String username);

    MemberInfo findMemberInfo(Authentication authentication);
}
