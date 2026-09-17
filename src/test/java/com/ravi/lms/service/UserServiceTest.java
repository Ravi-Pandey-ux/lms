package com.ravi.lms.service;

import com.ravi.lms.dto.UserRegisterRequest;
import com.ravi.lms.dto.UserResponse;
import com.ravi.lms.entity.User;
import com.ravi.lms.exception.DuplicateResourceException;
import com.ravi.lms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_shouldReturnUserResponse() {
        UserRegisterRequest request = new UserRegisterRequest("student1", "pass123", "student1@test.com", "STUDENT");
        when(userRepository.findByEmail("student1@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass123")).thenReturn("hashedpassword123");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("student1");
        savedUser.setEmail("student1@test.com");
        savedUser.setPassword("hashedPassword123");
        savedUser.setRole(User.Role.STUDENT);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        UserResponse response = userService.registerUser(request);
        assertNotNull(response);
        assertEquals("student1", response.username());
        assertEquals("student1@test.com", response.email());
        assertEquals("STUDENT", response.role());
    }

    @Test
    void registerUser_shouldThrowException_whenEmailAlreadyExists() {
        UserRegisterRequest request = new UserRegisterRequest("student1", "pass123", "student1@test.com", "STUDENT");
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("student1@test.com");

        when(userRepository.findByEmail("student1@test.com")).thenReturn(Optional.of(existingUser));

        assertThrows(DuplicateResourceException.class, () -> {
            userService.registerUser(request);
        });


    }
}
