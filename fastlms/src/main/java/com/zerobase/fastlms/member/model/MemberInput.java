package com.zerobase.fastlms.member.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberInput {
    private String userId;
    private String userName;
    private String password;
    private String phone;

    private String newPassword;

    private String zipCode;
    private String address;
    private String addressDetail;
}
