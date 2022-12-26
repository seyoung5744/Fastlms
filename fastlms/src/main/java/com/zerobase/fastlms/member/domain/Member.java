package com.zerobase.fastlms.member.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member implements MemberCode{

    @Id
    private String userId;

    private String userName;
    private String password;
    private String phone;

    @CreatedDate
    private LocalDateTime registerDt;

    private LocalDateTime updateDt;

    // 마지막 로그인 일자
    private LocalDateTime lastLoginDt;

    // application.yml에서
    // generate-ddl: true, ddl-auto: update가 True가 되어 있으면
    // 속성 추가만을 통해 DB에 컬럼이 자동 생성됨.

    // 이메일 인증 여부
    private boolean emailAuthYn;
    private LocalDateTime emailAuthDt;

    // 메일을 통해서 인증키를 보내주면 해당 인증키가 맞는지를 검사하기 위해 임의의 값을 저장하여 값 비교
    // 회원가입할 때 key를 만들어서 이메일로 보내주고 보내준 이메일 링크를 통해 인증 절차 진행
    private String emailAuthKey;

    // 해당 key가 일치하는 사용자만 접속을 했을 때 비밀번호 초기화 진행
    private String resetPasswordKey;

    // 링크를 타고 왔을 때 해당 기간 이후에는 비밀번호 초기화 불가능
    private LocalDateTime resetPasswordLimitDt;

    // 관리자 여부를 지정할거냐?
    // ex) true, false
    // 회원에 따른 ROLE을 지정할꺼냐?
    // ex) 준회원/ 정회원/ 특별회원/ 관리자
    // ROLE_SEMI_USER/ ROLE_USER/ ROLE_SPECIAL_USER/ ROLE_ADMIN
    private boolean adminYn;

    // 회원 상태
    // 이용 가능 상태, 정지 상태
    private String userStatus;

    // 우편번호
    private String zipCode;
    private String address;
    private String addressDetail;
}
