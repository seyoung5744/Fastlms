package com.zerobase.fastlms.course.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long categoryId;

    private String imagePath;
    private String keyword;
    private String subject;

    @Column(length = 1000)
    private String summary;

    // 많은 데이터가 저장됨.
    @Lob
    private String contents;
    private long price;
    private long salePrice;

    // 할인 종료 일
    private LocalDate saleEndDt;

    // 파일명
    private String filename;
    private String urlFilename;

    // 강좌 등록 날짜
    private LocalDateTime registerDt;

    // 강좌 업데이트(수정) 날짜
    private LocalDateTime updateDt;
}
