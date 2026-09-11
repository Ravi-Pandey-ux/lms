package com.ravi.lms.dto;

public record CourseResponse(Long id, String title, String description, Integer capacity, UserResponse instructor) {
}
