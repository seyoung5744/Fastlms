package com.zerobase.fastlms.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberInfo {
    private String userId;
    private String userStatus;
    private String password;
}
