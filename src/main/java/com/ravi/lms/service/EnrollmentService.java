package com.ravi.lms.service;

import com.ravi.lms.dto.CourseResponse;
import com.ravi.lms.dto.EnrollmentRequest;
import com.ravi.lms.dto.EnrollmentResponse;
import com.ravi.lms.dto.UserResponse;
import com.ravi.lms.entity.Course;
import com.ravi.lms.entity.Enrollment;
import com.ravi.lms.entity.User;
import com.ravi.lms.exception.CapacityExceededException;
import com.ravi.lms.exception.DuplicateResourceException;
import com.ravi.lms.exception.ResourceNotFoundException;
import com.ravi.lms.repository.CourseRepository;
import com.ravi.lms.repository.EnrollmentRepository;
import com.ravi.lms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, UserRepository userRepository,
                             CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public EnrollmentResponse enrollStudent(EnrollmentRequest request) {
        User student = userRepository.findById(request.studentId()).orElseThrow(() -> new ResourceNotFoundException("student not found "));
        Course course = courseRepository.findById(request.courseId()).orElseThrow(() -> new ResourceNotFoundException("course not found"));
        long currentEnrollments = enrollmentRepository.countByCourseId(request.courseId());
        if (currentEnrollments >= course.getCapacity()) {
            throw new CapacityExceededException("course capacity exceeded");
        }
        if (enrollmentRepository.existsByStudentIdAndCourseId(request.studentId(), request.courseId())) {
            throw new DuplicateResourceException("student already enrolled in this course");
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setStatus(Enrollment.Status.ACTIVE);
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return mapToResponse(savedEnrollment);
    }

    private EnrollmentResponse mapToResponse(Enrollment enrollment) {
        UserResponse studentResponse = new UserResponse(enrollment.getStudent().getId(), enrollment.getStudent().getUsername()
                , enrollment.getStudent().getEmail(), enrollment.getStudent().getRole().toString());

        CourseResponse courseResponse = new CourseResponse(enrollment.getCourse().getId(), enrollment.getCourse().getTitle(),
                enrollment.getCourse().getDescription(), enrollment.getCourse().getCapacity(), null);

        return new EnrollmentResponse(enrollment.getId(), studentResponse, courseResponse, enrollment.getEnrolledAt(),
                enrollment.getStatus().toString());
    }
}
