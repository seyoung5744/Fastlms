package com.zerobase.fastlms.admin.dto;

import com.zerobase.fastlms.member.domain.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberDto {
    private String userId;
    private String userName;
    private String phone;
    private String password;
    private LocalDateTime registerDt;
    private LocalDateTime updateDt;


    private boolean emailAuthYn;
    private LocalDateTime emailAuthDt;
    private String emailAuthKey;

    private String resetPasswordKey;
    private LocalDateTime resetPasswordLimitDt;

    private boolean adminYn;
    private String userStatus;

    // 우편번호
    private String zipCode;
    private String address;
    private String addressDetail;

    // 필요에 의한 추가...원래 여기 잇으면 안돼
    private long totalCount;

    // 테이블 NO 셋팅
    private long sequential;

    // 마지막 로그인 일자
    private LocalDateTime lastLoginDt;



    public static MemberDto of(Member memeber){
        return MemberDto.builder()
                .userId(memeber.getUserId())
                .userName(memeber.getUserName())
                .phone(memeber.getPhone())

                .registerDt(memeber.getRegisterDt())
                .updateDt(memeber.getUpdateDt())

                .emailAuthYn(memeber.isEmailAuthYn())
                .emailAuthDt(memeber.getEmailAuthDt())
                .emailAuthKey(memeber.getEmailAuthKey())

                .resetPasswordKey(memeber.getResetPasswordKey())
                .resetPasswordLimitDt(memeber.getResetPasswordLimitDt())

                .adminYn(memeber.isAdminYn())
                .userStatus(memeber.getUserStatus())

                .lastLoginDt(memeber.getLastLoginDt())

                .zipCode(memeber.getZipCode())
                .address(memeber.getAddress())
                .addressDetail(memeber.getAddressDetail())
                .build();
    }

    public String getLastLoginText(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return lastLoginDt != null ? this.lastLoginDt.format(dateTimeFormatter) : "";
    }
    public String getRegisterDtText() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return registerDt != null ? this.registerDt.format(dateTimeFormatter) : "";
    }

    public String getUpdateDtText() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return updateDt != null ? this.updateDt.format(dateTimeFormatter) : "";
    }
}
