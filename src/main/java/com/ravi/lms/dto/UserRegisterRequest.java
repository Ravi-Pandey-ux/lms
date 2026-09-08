package com.ravi.lms.dto;

public record UserRegisterRequest(
        String username,  String  password, String email, String role
) {
}
