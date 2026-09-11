    package com.ravi.lms.dto;

    public record CourseCreateRequest(String title, String description, Integer capacity, Long instructorId) {
    }
