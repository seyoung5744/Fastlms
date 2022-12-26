package com.zerobase.fastlms.course.service;

import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.dto.TakeCourseDto;
import com.zerobase.fastlms.course.model.*;

import java.util.List;

public interface TakeCourseService {

    /**
     * 수강 목록
     */
    List<TakeCourseDto> list(TakeCourseParam takeCourseParam);

    /**
     * 수강 상세 정보
     */
    TakeCourseDto detail(long id);
    /**
     * 수강 내용 상태 변경
     */
    ServiceResult updateStatus(Long id, String status);

    /**
     * 내 수강 내역 목록
     */
    List<TakeCourseDto> myCourse(String userId);


    /**
     * 수강 신청 취소
     */
    ServiceResult cancel(Long id);
}
