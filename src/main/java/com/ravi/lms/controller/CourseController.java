package com.ravi.lms.controller;

import com.ravi.lms.dto.CourseCreateRequest;
import com.ravi.lms.dto.CourseResponse;
import com.ravi.lms.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<Page<CourseResponse>> getAllCourses(Pageable pageable) {
        Page<CourseResponse> courseResponses = courseService.getAllCourses(pageable);
        return ResponseEntity.ok(courseResponses);
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<Page<CourseResponse>> getCourseByInstructor(@PathVariable Long instructorId, Pageable pageable) {
        Page<CourseResponse> courseResponses = courseService.getCoursesByInstructor(instructorId, pageable);
        return ResponseEntity.ok(courseResponses);
    }

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseCreateRequest request) {
        CourseResponse response = courseService.createCourse(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
