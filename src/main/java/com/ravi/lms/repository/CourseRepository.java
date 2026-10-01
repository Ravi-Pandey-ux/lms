package com.ravi.lms.repository;

import com.ravi.lms.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByInstructorId(Long instructorId, Pageable pageable);
}
