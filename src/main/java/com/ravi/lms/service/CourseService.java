package com.ravi.lms.service;

import com.ravi.lms.entity.Course;
import com.ravi.lms.entity.User;
import com.ravi.lms.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course createCourse(Course course) {
        if (course.getInstructor().getRole() != User.Role.INSTRUCTOR) {
            throw new RuntimeException("Only instructors can create courses");
        }
        return courseRepository.save(course);
    }

    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }


}
