package com.scheduler.memberservice.client.controller;

import com.scheduler.memberservice.testSet.IntegrationTest;
import com.scheduler.memberservice.testSet.student.WithStudent;
import com.scheduler.memberservice.testSet.teacher.WithTeacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static com.scheduler.memberservice.testSet.TestUserHeaders.userHeaders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class FeignCourseControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithTeacher(username = "lee_teacher")
    void findTeacherInfo() throws Exception {

        mockMvc.perform(get("/feign-course-member/teacher/info")
                        .with(userHeaders()))
                .andExpect(status().isOk());
    }

    @Test
    @WithStudent(username = "lee_student")
    void findStudentInfo() throws Exception {

        mockMvc.perform(get("/feign-course-member/student/info")
                        .with(userHeaders()))
                .andExpect(status().isOk());

    }

    @Test
    @WithTeacher(username = "lee_teacher")
    void findMemberInfo() throws Exception {

        mockMvc.perform(get("/feign-course-member/member/info")
                        .with(userHeaders()))
                .andExpect(status().isOk());
    }

}