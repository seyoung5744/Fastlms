package com.zerobase.fastlms.banner.repository;

import com.zerobase.fastlms.banner.domain.Banner;
import com.zerobase.fastlms.banner.dto.BannerDto;
import com.zerobase.fastlms.course.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface BannerRepository extends JpaRepository<Banner, Long> {
    Optional<List<Banner>> findByOpenYnIsTrueOrderBySequenceAsc();
    boolean existsBySequence(int sequence);
}
