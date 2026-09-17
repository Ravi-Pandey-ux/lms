package com.ravi.lms.service;

import com.ravi.lms.dto.EnrollmentRequest;
import com.ravi.lms.dto.EnrollmentResponse;
import com.ravi.lms.entity.Course;
import com.ravi.lms.entity.Enrollment;
import com.ravi.lms.entity.User;
import com.ravi.lms.exception.CapacityExceededException;
import com.ravi.lms.exception.ResourceNotFoundException;
import com.ravi.lms.repository.CourseRepository;
import com.ravi.lms.repository.EnrollmentRepository;
import com.ravi.lms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {
    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;


    @Test
    void enrollStudent_shouldReturnEnrollmentResponse_whenAllValid() {
        User student = new User();
        student.setId(1L);
        student.setUsername("student1");
        student.setEmail("student1@test.com");
        student.setRole(User.Role.STUDENT);

        Course course = new Course();
        course.setId(1L);
        course.setTitle("Spring Boot Basics");
        course.setDescription("Learn Spring Boot");
        course.setCapacity(5);

        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.countByCourseId(1L)).thenReturn(2L);
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(false);

        Enrollment savedEnrollment = new Enrollment();
        savedEnrollment.setId(100L);
        savedEnrollment.setStudent(student);
        savedEnrollment.setCourse(course);
        savedEnrollment.setEnrolledAt(LocalDateTime.now());
        savedEnrollment.setStatus(Enrollment.Status.ACTIVE);

        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(savedEnrollment);
        EnrollmentRequest request = new EnrollmentRequest(1L, 1L);
        EnrollmentResponse response = enrollmentService.enrollStudent(request);

        assertNotNull(response);
        assertEquals(100L, response.id());
        assertEquals("student1", response.student().username());
        assertEquals("Spring Boot Basics", response.course().title());
        assertEquals("ACTIVE", response.status());

        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void enrollStudent_shouldThrowException_whenStudentNotFound() {
        EnrollmentRequest request = new EnrollmentRequest(1L, 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> enrollmentService.enrollStudent(request));
        verify(enrollmentRepository, times(0)).save(any(Enrollment.class));
    }

    @Test
    void enrollStudent_shouldThrowException_whenCapacityExceeded() {
        EnrollmentRequest request = new EnrollmentRequest(1L, 1L);
        User student = new User();
        student.setId(1L);

        Course course = new Course();
        course.setId(1L);
        course.setCapacity(2);
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        when(enrollmentRepository.countByCourseId(1L)).thenReturn(2L);
        assertThrows(CapacityExceededException.class, () -> enrollmentService.enrollStudent(request));
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

}
