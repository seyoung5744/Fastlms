package com.zerobase.fastlms.course.model;

import com.zerobase.fastlms.admin.model.CommonParam;
import lombok.Data;

@Data
public class TakeCourseParam extends CommonParam {
     private Long id;
     private String status;
     private String userId;



     private Long searchCourseId;
}
