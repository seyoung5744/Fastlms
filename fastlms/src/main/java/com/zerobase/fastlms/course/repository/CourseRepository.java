package com.zerobase.fastlms.course.repository;

import com.zerobase.fastlms.course.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CourseRepository extends JpaRepository<Course, Long> {
    /**
     * 카테고리 id 별 강의 목록
     */
    Optional<List<Course>> findByCategoryId(long categoryId);
}
