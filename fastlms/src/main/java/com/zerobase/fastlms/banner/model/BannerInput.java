package com.zerobase.fastlms.banner.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BannerInput {
    private Long id;

    // 배너명
    private String subject;

    // 파일명
    private String filename;

    private String urlFilename;

    // 오픈 방법
    // 상태(수강신청, 결재완료, 수강취소)
    private String status;

    // 공개 여부
    private boolean openYn;

    // 정렬 순서
    private int sequence;

    // 삭제를 위한 id 배열 정보
    String idList;
}
