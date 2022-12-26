package com.zerobase.fastlms.course.model;

import com.zerobase.fastlms.admin.model.CommonParam;
import lombok.Data;

@Data
public class CourseParam extends CommonParam {
    // 강좌 상세보기 id
    private long id;

    // category id 에 따른 강좌 목록
    private long categoryId;
}
