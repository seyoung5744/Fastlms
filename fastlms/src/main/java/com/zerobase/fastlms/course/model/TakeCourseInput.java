package com.zerobase.fastlms.course.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TakeCourseInput {
    private String userId;
    private Long courseId;

    private Long takeCourseId; // take_course.id
}
