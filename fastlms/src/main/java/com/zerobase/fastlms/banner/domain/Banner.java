package com.zerobase.fastlms.banner.domain;

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
public class Banner implements BannerOpenCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 배너명
    private String subject;

    // 원본 파일명
    private String filename;

    // 링크 주소
    private String urlFilename;

    // 오픈 방법
    // 상태(수강신청, 결재완료, 수강취소)
    private String status;

    // 공개 여부
    private boolean openYn;

    // 정렬 순서
    private int sequence;

    // 배너 등록 날짜
    private LocalDateTime registerDt;

    // 배너 업데이트(수정) 날짜
    private LocalDateTime updateDt;
}
