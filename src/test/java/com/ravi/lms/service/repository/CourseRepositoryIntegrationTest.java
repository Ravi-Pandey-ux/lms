package com.ravi.lms.service.repository;

import com.ravi.lms.config.CacheConfig;
import com.ravi.lms.entity.Course;
import com.ravi.lms.entity.User;
import com.ravi.lms.repository.CourseRepository;
import com.ravi.lms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Testcontainers
@Import(CacheConfig.class)
public class CourseRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>();

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
        registry.add("spring.cache.type", () -> "redis");
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