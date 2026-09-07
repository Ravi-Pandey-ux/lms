package com.ravi.lms.service;

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

    public Enrollment enrollStudent(Long studentId, Long courseId) {
        User student = userRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("student not found "));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));
        long currentEnrollments = enrollmentRepository.countByCourseId(courseId);
        if (currentEnrollments >= course.getCapacity()) {
            throw new CapacityExceededException("course capacity exceeded");
        }
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new DuplicateResourceException("student already enrolled in this course");
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setStatus(Enrollment.Status.ACTIVE);
        return enrollmentRepository.save(enrollment);

    }
}
