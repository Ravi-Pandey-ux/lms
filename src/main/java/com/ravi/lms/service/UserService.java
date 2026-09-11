package com.ravi.lms.service;

import com.ravi.lms.dto.UserRegisterRequest;
import com.ravi.lms.dto.UserResponse;
import com.ravi.lms.entity.User;
import com.ravi.lms.exception.DuplicateResourceException;
import com.ravi.lms.exception.ResourceNotFoundException;
import com.ravi.lms.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public UserResponse registerUser(UserRegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new DuplicateResourceException("Email already exists");
        }
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setRole(User.Role.valueOf(request.role()));
        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole().toString());

    }


    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole().toString());

    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(user -> new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole().toString())).toList();

    }
}
