package com.zerobase.fastlms.course.repository;

import com.zerobase.fastlms.course.domain.TakeCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;


public interface TakeCourseRepository extends JpaRepository<TakeCourse, Long> {
    long countByCourseIdAndUserIdAndStatusIn(Long courseId, String userId, Collection<String> statusList);
}
