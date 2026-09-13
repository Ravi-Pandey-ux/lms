package com.ravi.lms.dto;

public record EnrollmentRequest(
        Long studentId,
        Long courseId
) {
}
