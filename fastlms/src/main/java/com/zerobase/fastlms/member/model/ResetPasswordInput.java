package com.zerobase.fastlms.member.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@ToString
public class ResetPasswordInput {
    private String userId;
    private String userName;
    private String id; // uuid
    private String password;
}
