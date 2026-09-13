package com.ravi.lms.controller;

import com.ravi.lms.dto.EnrollmentRequest;
import com.ravi.lms.dto.EnrollmentResponse;
import com.ravi.lms.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public ResponseEntity<EnrollmentResponse> enroll(@RequestBody EnrollmentRequest enrollmentRequest) {
        EnrollmentResponse response = enrollmentService.enrollStudent(enrollmentRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
