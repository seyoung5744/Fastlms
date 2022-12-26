package com.zerobase.fastlms.course.dto;

import com.zerobase.fastlms.course.domain.Course;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CourseDto {

    private Long id;

    private Long categoryId;

    private String imagePath;
    private String keyword;
    private String subject;

    private String summary;

    // 많은 데이터가 저장됨.
    private String contents;
    private long price;
    private long salePrice;
    private LocalDate saleEndDt;

    // 강좌 등록 날짜
    private LocalDateTime registerDt;

    // 강좌 업데이트(수정) 날짜
    private LocalDateTime updateDt;

    // 파일명
    private String filename;
    private String urlFilename;

    private long totalCount;
    private long sequential;

    public static CourseDto of(Course course) {
        return CourseDto.builder()
                .id(course.getId())
                .categoryId(course.getCategoryId())
                .imagePath(course.getImagePath())
                .keyword(course.getKeyword())
                .subject(course.getSubject())
                .summary(course.getSummary())
                .contents(course.getContents())
                .price(course.getPrice())
                .salePrice(course.getSalePrice())
                .saleEndDt(course.getSaleEndDt())
                .registerDt(course.getRegisterDt())
                .updateDt(course.getUpdateDt())
                .filename(course.getFilename())
                .urlFilename(course.getUrlFilename())
                .build();
    }

    public static List<CourseDto> of(List<Course> courseList) {
        if(courseList == null){
            return null;
        }
        return courseList.stream().map(CourseDto::of).collect(Collectors.toList());
    }
}
