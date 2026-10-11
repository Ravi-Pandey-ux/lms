package com.ravi.lms.dto;

import java.io.Serializable;

public record UserResponse(Long id, String username, String email, String role) implements Serializable {
}
