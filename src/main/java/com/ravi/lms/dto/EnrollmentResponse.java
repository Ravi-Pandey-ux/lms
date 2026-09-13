package com.ravi.lms.dto;

import java.time.LocalDateTime;

public record EnrollmentResponse(
        Long id,
        UserResponse student,
        CourseResponse course,
        LocalDateTime enrolledAt,
        String status
) {
}
