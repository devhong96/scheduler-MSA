package com.scheduler.courseservice.course.controller;

import com.scheduler.courseservice.client.MemberServiceClient;
import com.scheduler.courseservice.testSet.IntegrationTest;

import static com.scheduler.courseservice.testSet.TestUserHeaders.asAdmin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class AdminCourseControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private MemberServiceClient memberServiceClient;

    @Test
    @DisplayName("컨트롤러 : 관리자 수업 조회")
    void findAllStudentsCourses() throws Exception{

        mockMvc.perform(get("/admin/class")
                        .with(asAdmin("test_admin")))
                .andExpect(status().isOk());
    }

}