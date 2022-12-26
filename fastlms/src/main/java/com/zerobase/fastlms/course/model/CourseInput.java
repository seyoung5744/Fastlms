package com.zerobase.fastlms.course.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CourseInput {
    private Long id;
    private Long categoryId;
    private String subject;
    private String keyword;


    private String summary;

    private String contents;
    private long price;
    private long salePrice;

    // 정상적으로 날짜 데이터가 전달된다는 확신이 없기 떄문에
    // 날짜 데이터로 변환 예정
    private String saleEndDtText;

    // 삭제를 위한 id 배열 정보
    String idList;

    // 파일명
    private String filename;
    private String urlFilename;
}
