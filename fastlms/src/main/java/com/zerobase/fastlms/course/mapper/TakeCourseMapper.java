package com.zerobase.fastlms.course.mapper;

import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.dto.TakeCourseDto;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.model.TakeCourseParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TakeCourseMapper {
    long selectListCount(TakeCourseParam takeCourseParam);

    List<TakeCourseDto> selectList(TakeCourseParam takeCourseParam);

    List<TakeCourseDto> selectListMyCourse(TakeCourseParam takeCourseParam);
}
