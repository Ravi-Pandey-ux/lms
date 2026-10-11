package com.ravi.lms.dto;

import java.io.Serializable;

public record CourseResponse(Long id, String title, String description, Integer capacity,
                             UserResponse instructor) implements Serializable {
}
