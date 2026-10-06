package com.ravi.lms.service;

import com.ravi.lms.dto.CourseCreateRequest;
import com.ravi.lms.dto.CourseResponse;
import com.ravi.lms.dto.UserResponse;
import com.ravi.lms.entity.Course;
import com.ravi.lms.entity.User;
import com.ravi.lms.exception.ResourceNotFoundException;
import com.ravi.lms.repository.CourseRepository;
import com.ravi.lms.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @CacheEvict(value = "courses", allEntries = true)
    public CourseResponse createCourse(CourseCreateRequest request) {
        User instructor = userRepository.findById(request.instructorId()).orElseThrow(() -> new ResourceNotFoundException("instructor not found"));

        Course course = new Course();
        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setInstructor(instructor);
        course.setCapacity(request.capacity());
        Course savedCourse = courseRepository.save(course);
        UserResponse instructorResponse = new UserResponse(instructor.getId(), instructor.getUsername(), instructor.getEmail(), instructor.getRole().toString());

        return new CourseResponse(savedCourse.getId(), savedCourse.getTitle(), savedCourse.getDescription(), savedCourse.getCapacity(), instructorResponse);
    }

    public Page<CourseResponse> getCoursesByInstructor(Long instructorId, Pageable pageable) {
        return courseRepository.findByInstructorId(instructorId, pageable).map(course -> {
            UserResponse instructorResponse = new UserResponse(course.getInstructor().getId(), course.getInstructor().getUsername(), course.getInstructor().getEmail(), course.getInstructor().getRole().toString());
            return new CourseResponse(course.getId(), course.getTitle(), course.getDescription(), course.getCapacity(), instructorResponse);
        });
    }

    @Cacheable(value = "courses")
    public Page<CourseResponse> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable).map(course -> {
            UserResponse instructorResponse = new UserResponse(course.getInstructor().getId(), course.getInstructor().getUsername(), course.getInstructor().getEmail(), course.getInstructor().getRole().toString());
            return new CourseResponse(course.getId(), course.getTitle(), course.getDescription(), course.getCapacity(), instructorResponse);
        });
    }


}
