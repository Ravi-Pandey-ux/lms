package com.ravi.lms.service.repository;

import com.ravi.lms.entity.Course;
import com.ravi.lms.entity.User;
import com.ravi.lms.repository.CourseRepository;
import com.ravi.lms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.PageRequest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Testcontainers
public class CourseRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>();

    @org.springframework.test.context.DynamicPropertySource
    static void configureProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByInstructorId_shouldReturnCourses_whenSavedToRealDatabase() {
        User instructor = new User();
        instructor.setUsername("instructor1");
        instructor.setEmail("instructor1@test.com");
        instructor.setPassword("hashedpass");
        instructor.setRole(User.Role.INSTRUCTOR);
        instructor = userRepository.save(instructor);

        Course course = new Course();
        course.setTitle("Spring Boot Basics");
        course.setDescription("Learn Spring Boot");
        course.setCapacity(2);
        course.setInstructor(instructor);
        courseRepository.save(course);

        var result = courseRepository.findByInstructorId(instructor.getId(), PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Spring Boot Basics", result.getContent().get(0).getTitle());
    }
}
