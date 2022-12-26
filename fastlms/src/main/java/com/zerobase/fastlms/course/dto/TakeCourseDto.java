package com.zerobase.fastlms.course.dto;

import com.zerobase.fastlms.course.domain.TakeCourse;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TakeCourseDto {

    private Long id;

    private Long courseId;

    private String userId;

    // 결제 금액
    private Long payPrice;

    // 상태(수강신청, 결재완료, 수강취소)
    private String status;

    // 신청일
    private LocalDateTime registerDt;

    // Join
    private String userName;
    private String phone;
    private String subject;

    private long totalCount;
    private long sequential;

    public static TakeCourseDto of(TakeCourse takeCourse) {
        return TakeCourseDto.builder()
                .id(takeCourse.getId())
                .courseId(takeCourse.getCourseId())
                .userId(takeCourse.getUserId())

                .payPrice(takeCourse.getPayPrice())
                .status(takeCourse.getStatus())

                .registerDt(takeCourse.getRegisterDt())
                .build();
    }

    public String getRegisterDtText() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return registerDt != null ? this.registerDt.format(dateTimeFormatter) : "";
    }
}
