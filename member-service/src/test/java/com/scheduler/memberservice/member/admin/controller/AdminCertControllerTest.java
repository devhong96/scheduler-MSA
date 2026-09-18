package com.scheduler.memberservice.member.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.memberservice.testSet.IntegrationTest;
import com.scheduler.memberservice.testSet.admin.WithAdmin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static com.scheduler.memberservice.testSet.TestUserHeaders.userHeaders;
import static com.scheduler.memberservice.member.admin.dto.AdminInfoRequest.EditEmailRequest;
import static com.scheduler.memberservice.member.admin.dto.AdminInfoRequest.PwdEditRequest;
import static com.scheduler.memberservice.testSet.TestConstants.TEST_ADMIN_USERNAME;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class AdminCertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("관리자 비밀번호 초기화")
    @WithAdmin(username = TEST_ADMIN_USERNAME)
    void initializePassword() throws Exception {

        PwdEditRequest pwdEditRequest = new PwdEditRequest();
        pwdEditRequest.setPassword("password");

        String json = objectMapper.writeValueAsString(pwdEditRequest);

        mockMvc.perform(patch("/admin/help/password")
                        .with(userHeaders())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("관리자 이메일 변경")
    @WithAdmin(username = TEST_ADMIN_USERNAME)
    void updateEmail() throws Exception {

        EditEmailRequest editEmailRequest = new EditEmailRequest();
        editEmailRequest.setEmail("testEmail@gmail.com");

        String json = objectMapper.writeValueAsString(editEmailRequest);

        mockMvc.perform(patch("/admin/help/email")
                        .with(userHeaders())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

}