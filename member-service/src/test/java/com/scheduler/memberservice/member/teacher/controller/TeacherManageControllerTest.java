package com.scheduler.memberservice.member.teacher.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.scheduler.memberservice.client.CourseServiceClient;
import com.scheduler.memberservice.testSet.IntegrationTest;
import com.scheduler.memberservice.testSet.admin.WithAdmin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.scheduler.memberservice.testSet.TestUserHeaders.userHeaders;
import static com.scheduler.memberservice.client.dto.FeignMemberRequest.CourseExistenceResponse;
import static com.scheduler.memberservice.testSet.TestConstants.TEST_ADMIN_USERNAME;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class TeacherManageControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WireMockServer wireMockServer;

    @MockitoBean
    private CourseServiceClient courseServiceClient;
    //== setting

    @BeforeEach
    void startWireMockServer() {
        if (!wireMockServer.isRunning()) {
            wireMockServer.start();
        }
    }

    @AfterEach
    void stopWireMockServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }


    @Test
    @WithAdmin(username = TEST_ADMIN_USERNAME)
    void getTeacherList() throws Exception{

        mockMvc.perform(get("/manage/teacher/list")
                        .with(userHeaders()))
                .andExpect(status().isOk());
    }

    @Test
    @WithAdmin(username = TEST_ADMIN_USERNAME)
    void findTeacherInformation() throws Exception {

        mockMvc.perform(get("/manage/teacher/lee_teacher")
                        .with(userHeaders()))
                .andExpect(status().isOk());
    }

    @Test
    @WithAdmin(username = TEST_ADMIN_USERNAME)
    void changeTeacherStatus() throws Exception {

        when(courseServiceClient.existWeeklyCoursesByTeacherId("TCH001"))
                .thenReturn(new CourseExistenceResponse(false));

        mockMvc.perform(patch("/manage/teacher/lee_teacher/status")
                        .with(userHeaders()))
                .andExpect(status().isOk());
    }

}