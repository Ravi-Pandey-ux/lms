package com.ravi.lms.dto;

public record LoginResponse(String token,
                            String type,
                            Long id,
                            String email,
                            String username,
                            String role) {
}
