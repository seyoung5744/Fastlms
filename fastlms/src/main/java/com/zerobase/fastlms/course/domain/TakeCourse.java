package com.zerobase.fastlms.course.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class TakeCourse implements TakeCourseCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long courseId;

    private String userId;

    // 결제 금액
    private Long payPrice;

    // 상태(수강신청, 결재완료, 수강취소)
    private String status;

    // 신청일
    private LocalDateTime registerDt;
}
