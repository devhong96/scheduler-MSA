package com.scheduler.orderservice.infra.health;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/health-check")
@RequiredArgsConstructor
public class HealthCheckController {

    private final TestService testService;

    @Operation(
            summary = "네트워크 및 어플리케이션 상태 확인"
    )
    @GetMapping
    public ResponseEntity<Void> healthCheck() {
        return new ResponseEntity<>(OK);
    }

    @GetMapping("/test-user")
    public ResponseEntity<StudentResponse> testComponent(
            @AuthenticationPrincipal(expression = "username") String username
    ) {
        return new ResponseEntity<>(testService.test(username), OK);
    }

    @PostMapping("/test-order/{studentId}/{username}/{quantity}")
    public ResponseEntity<StudentResponse> testOrder(
            @PathVariable String studentId, @PathVariable String username, @PathVariable Integer quantity
    ) {
        testService.test(studentId, username, quantity);
        return new ResponseEntity<>(OK);
    }
}
