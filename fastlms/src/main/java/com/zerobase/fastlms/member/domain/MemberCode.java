package com.zerobase.fastlms.member.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

public interface MemberCode {
    /**
     * 현재 가입 요청 중
     */
    String MEMBER_STATUS_REQ = "REQ";

    /**
     * 현재 이용 중인 상태
     */
    String MEMBER_STATUS_ING = "ING";

    /**
     * 현재 정지된 상태
     */
    String MEMBER_STATUS_STOP = "STOP";

    /**
     * 회원 탈퇴
     */
    String MEMBER_STATUS_WITHDRAW = "WITHDRAW";

}
