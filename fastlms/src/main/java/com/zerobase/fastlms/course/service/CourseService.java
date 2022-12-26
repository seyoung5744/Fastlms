package com.zerobase.fastlms.course.service;

import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.model.TakeCourseInput;

import java.util.List;

public interface CourseService {
    /**
     * 강좌 등록
     */
    boolean add(CourseInput courseInput);

    /**
     * 강좌 정보 수정
     */
    boolean set(CourseInput courseInput);

    /**
     * 강좌 목록
     */
    List<CourseDto> list(CourseParam courseParam);

    /**
     * 강좌 상세 정보
     */
    CourseDto getById(long id);


    /**
     * 목록 중 강좌 삭제
     */
    boolean delete(String idList);

    /**
     * 프론트 강좌 목록
     */
    List<CourseDto> frontList(CourseParam courseParam);

    /**
     * 프론트 강좌 상세 정보 조회
     */
    CourseDto frontDetail(long id);

    /**
     *
     * @param takeCourseInput : 강좌 id, 계정 id
     */
    ServiceResult register(TakeCourseInput takeCourseInput);

    /**
     * 전체 강좌 목록
     */
    List<CourseDto> listAll();
}
