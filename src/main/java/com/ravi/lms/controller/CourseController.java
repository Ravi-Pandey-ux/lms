package com.ravi.lms.controller;

import com.ravi.lms.dto.CourseCreateRequest;
import com.ravi.lms.dto.CourseResponse;
import com.ravi.lms.entity.Course;
import com.ravi.lms.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/instructor/{instructorId}")
    public List<Course> getCourseByInstructor(@PathVariable Long instructorId) {
        return courseService.getCoursesByInstructor(instructorId);
    }

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseCreateRequest request) {
        CourseResponse response=courseService.createCourse(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
