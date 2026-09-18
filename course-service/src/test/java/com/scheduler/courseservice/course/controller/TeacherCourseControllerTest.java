package com.scheduler.courseservice.course.controller;

import com.scheduler.courseservice.client.MemberServiceClient;
import com.scheduler.courseservice.testSet.IntegrationTest;

import static com.scheduler.courseservice.testSet.TestUserHeaders.asTeacher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.scheduler.courseservice.client.dto.FeignMemberInfo.TeacherInfo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class TeacherCourseControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private MemberServiceClient memberServiceClient;

    @Test
    @DisplayName("컨트롤러 : 교사 수업 조회")
    void findTeachersClasses() throws Exception {

        TeacherInfo teacherInfo = new TeacherInfo("teacherId");

        when(memberServiceClient.findTeacherInfo())
                .thenReturn(teacherInfo);

        mockMvc.perform(get("/teacher/class")
                        .with(asTeacher("test_teacher")))
                .andExpect(status().isOk());
    }

}