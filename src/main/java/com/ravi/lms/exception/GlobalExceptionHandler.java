package com.ravi.lms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException exception) {
        return buildResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateResourceException exception) {
        return buildResponseEntity(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CapacityExceededException.class)
    public ResponseEntity<Map<String, Object>> handleCapacityExceeded(CapacityExceededException exception) {
        return buildResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Map<String, Object>> buildResponseEntity(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("status", status.value());
        response.put("timestamp", LocalDateTime.now().toString());
        return new ResponseEntity<>(response, status);
    }

}
