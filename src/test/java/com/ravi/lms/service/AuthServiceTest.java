package com.ravi.lms.service;

import com.ravi.lms.dto.LoginRequest;
import com.ravi.lms.dto.LoginResponse;
import com.ravi.lms.entity.Enrollment;
import com.ravi.lms.entity.User;
import com.ravi.lms.exception.InvalidCredentialsException;
import com.ravi.lms.exception.ResourceNotFoundException;
import com.ravi.lms.repository.UserRepository;
import com.ravi.lms.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_ValidCredentials_ReturnsToken() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@gamil.com");
        user.setRole(User.Role.STUDENT);

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(user.getUsername())).thenReturn("token2467");
        LoginRequest loginRequest = new LoginRequest("username", "password");

        LoginResponse loginResponse = authService.login(loginRequest);
        assertNotNull(loginResponse);
        assertEquals("token2467", loginResponse.token());
        assertEquals(1L, loginResponse.id());
        assertEquals("username", loginResponse.username());
        assertEquals("email@gamil.com", loginResponse.email());
        assertEquals("STUDENT", loginResponse.role());
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_invalidPassword_ThrowsException() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        LoginRequest loginRequest = new LoginRequest("username", "password");
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(false);
        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginRequest));

    }

}
