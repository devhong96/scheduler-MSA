package com.scheduler.memberservice.client.service;

import static com.scheduler.memberservice.client.dto.FeignMemberResponse.StudentResponse;

public interface FeignOrderService {

    StudentResponse getStudentInfo(String username);

    StudentResponse findStudentByUsername(String username);

}
