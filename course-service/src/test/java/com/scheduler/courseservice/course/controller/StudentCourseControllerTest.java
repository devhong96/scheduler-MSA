package com.scheduler.courseservice.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.courseservice.client.MemberServiceClient;
import com.scheduler.courseservice.testSet.IntegrationTest;

import static com.scheduler.courseservice.testSet.TestUserHeaders.asStudent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.scheduler.courseservice.client.dto.FeignMemberInfo.StudentInfo;
import static com.scheduler.courseservice.course.dto.CourseInfoRequest.UpsertCourseRequest;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class StudentCourseControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private MemberServiceClient memberServiceClient;

    @Test
    @DisplayName("컨트롤러 : 학생 수업 조회")
    void findStudentClasses() throws Exception {

        StudentInfo studentInfo = new StudentInfo("teacher_001", "Mr. Kim", "student_009", "Irene Seo");

        when(memberServiceClient.findStudentInfo())
                .thenReturn(studentInfo);

        mockMvc.perform(get("/student/class")
                        .with(asStudent("student_001")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("컨트롤러 : 학생 수업 조회")
    void applyCourse() throws Exception {
        StudentInfo studentInfo = new StudentInfo("teacher_001", "Mr. Kim", "student_009", "Irene Seo");

        when(memberServiceClient.findStudentInfo())
                .thenReturn(studentInfo);

        UpsertCourseRequest request = new UpsertCourseRequest();
        request.setMondayClassHour(1);
        request.setTuesdayClassHour(4);
        request.setWednesdayClassHour(3);
        request.setThursdayClassHour(2);
        request.setFridayClassHour(5);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/student/class")
                        .with(asStudent("student_001"))
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }


}