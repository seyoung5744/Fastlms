package com.zerobase.fastlms.banner.service;

import com.zerobase.fastlms.banner.dto.BannerDto;
import com.zerobase.fastlms.banner.model.BannerInput;
import com.zerobase.fastlms.banner.model.BannerParam;
import com.zerobase.fastlms.course.dto.CourseDto;

import java.util.List;

public interface BannerService {
    /**
     * 강좌 등록
     */
    boolean add(BannerInput bannerInput);

    /**
     * 강좌 정보 수정
     */
    boolean set(BannerInput bannerInput);
    /**
     * 강좌 상세 정보
     */
    BannerDto getById(long id);

    /**
     * 강좌 목록
     */
    List<BannerDto> list(BannerParam bannerParam);

    /**
     * 목록 중 강좌 삭제
     */
    boolean delete(String idList);

    /**
     * 전체 강좌 목록
     */
    List<BannerDto> listAll();

    /**
     * 순서 확인
     */
    boolean checkSequence(int sequence);
}
