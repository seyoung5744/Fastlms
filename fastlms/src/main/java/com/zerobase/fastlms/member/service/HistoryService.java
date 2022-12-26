package com.zerobase.fastlms.member.service;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.admin.dto.MemberLoginLogDto;
import com.zerobase.fastlms.admin.model.HistoryParam;
import com.zerobase.fastlms.admin.model.MemberParam;
import com.zerobase.fastlms.member.model.MemberInput;

import java.util.List;

public interface HistoryService {
    List<MemberLoginLogDto> list(HistoryParam historyParam);
}
