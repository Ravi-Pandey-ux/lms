package com.ravi.lms.service;

import com.ravi.lms.dto.CourseCreateRequest;
import com.ravi.lms.dto.CourseResponse;
import com.ravi.lms.dto.UserResponse;
import com.ravi.lms.entity.Course;
import com.ravi.lms.entity.User;
import com.ravi.lms.exception.ResourceNotFoundException;
import com.ravi.lms.repository.CourseRepository;
import com.ravi.lms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public CourseResponse createCourse(CourseCreateRequest request) {
        User instructor = userRepository.findById(request.instructorId())
                .orElseThrow(() -> new ResourceNotFoundException("instructor not found"));

        if (instructor.getRole() != User.Role.INSTRUCTOR) {
            throw new IllegalArgumentException("Only instructors can create courses");
        }
        Course course = new Course();
        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setInstructor(instructor);
        course.setCapacity(request.capacity());
        Course savedCourse = courseRepository.save(course);
        UserResponse instructorResponse = new UserResponse(
                instructor.getId(),
                instructor.getUsername(),
                instructor.getEmail()
                , instructor.getRole().toString()
        );

        return new CourseResponse(
                savedCourse.getId(),
                savedCourse.getTitle(),
                savedCourse.getDescription(),
                savedCourse.getCapacity(),
                instructorResponse);
    }

    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }


}
