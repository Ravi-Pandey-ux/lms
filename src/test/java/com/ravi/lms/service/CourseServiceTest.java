package com.ravi.lms.service;

import com.ravi.lms.dto.CourseCreateRequest;
import com.ravi.lms.dto.CourseResponse;
import com.ravi.lms.entity.Course;
import com.ravi.lms.entity.User;
import com.ravi.lms.exception.ResourceNotFoundException;
import com.ravi.lms.repository.CourseRepository;
import com.ravi.lms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.module.ResolutionException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    void createCourse_shouldReturnCourseResponse_whenInstructorExists() {
        User instructor = new User();
        instructor.setId(1L);
        instructor.setUsername("instructor1");
        instructor.setEmail("instructor1@test.com");
        instructor.setRole(User.Role.INSTRUCTOR);

        CourseCreateRequest request = new CourseCreateRequest("Spring Boot Basics", "Learn Spring Boot", 2, 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(instructor));

        Course savedCourse = new Course();
        savedCourse.setId(1L);
        savedCourse.setTitle("Spring Boot Basics");
        savedCourse.setDescription("Learn Spring Boot");
        savedCourse.setCapacity(2);
        savedCourse.setInstructor(instructor);

        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        CourseResponse courseResponse = courseService.createCourse(request);
        assertNotNull(courseResponse);
        assertEquals("Spring Boot Basics", courseResponse.title());
        assertEquals(2, courseResponse.capacity());
        assertEquals("instructor1", courseResponse.instructor().username());


    }

    @Test
    void createCourse_shouldThrowException_whenInstructorNotFound() {
        CourseCreateRequest request = new CourseCreateRequest("Spring Boot Basics", "Learn Spring Boot", 2, 99L);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.createCourse(request);
        });
    }
}
