package com.ravi.lms.service;

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

    public Course createCourse(Course course) {
        User instructor = userRepository.findById(course.getInstructor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("instructor not found"));

        if (instructor.getRole() != User.Role.INSTRUCTOR) {
            throw new IllegalArgumentException("Only instructors can create courses");
        }

        course.setInstructor(instructor);
        return courseRepository.save(course);
    }

    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }


}
