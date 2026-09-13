package com.ravi.lms.service;

import com.ravi.lms.dto.LoginRequest;
import com.ravi.lms.dto.LoginResponse;
import com.ravi.lms.entity.User;
import com.ravi.lms.exception.InvalidCredentialsException;
import com.ravi.lms.exception.ResourceNotFoundException;
import com.ravi.lms.repository.UserRepository;
import com.ravi.lms.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username()).orElseThrow(() ->
                new ResourceNotFoundException("username not found"));
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }
        String token = jwtUtil.generateToken(user.getUsername());
        return new LoginResponse(token, "Bearer", user.getId(), user.getEmail(), user.getUsername(), user.getRole().toString());
    }
}
