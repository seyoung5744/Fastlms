package com.zerobase.fastlms.banner.dto;

import com.zerobase.fastlms.banner.domain.Banner;
import com.zerobase.fastlms.course.domain.Course;
import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class BannerDto {
    private Long id;

    // 배너명
    private String subject;

    // 파일명
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

    // Page
    private long totalCount;
    private long sequential;

    public static BannerDto of(Banner banner) {
        return BannerDto.builder()
                .id(banner.getId())
                .subject(banner.getSubject())
                .filename(banner.getFilename())
                .urlFilename(banner.getUrlFilename())
                .status(banner.getStatus())
                .openYn(banner.isOpenYn())
                .sequence(banner.getSequence())
                .registerDt(banner.getRegisterDt())
                .build();
    }

    public static List<BannerDto> of(List<Banner> bannerList) {
        if(bannerList == null){
            return null;
        }
        return bannerList.stream().map(BannerDto::of).collect(Collectors.toList());
    }
}
