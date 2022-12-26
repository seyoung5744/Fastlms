package com.zerobase.fastlms.member.service.impl;

import com.zerobase.fastlms.admin.dto.MemberLoginLogDto;
import com.zerobase.fastlms.admin.mapper.HistoryMapper;
import com.zerobase.fastlms.admin.model.HistoryParam;
import com.zerobase.fastlms.member.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final HistoryMapper historyMapper;

    @Override
    public List<MemberLoginLogDto> list(HistoryParam historyParam) {
        long totalCount = historyMapper.selectListCount(historyParam);

        List<MemberLoginLogDto> list = historyMapper.selectList(historyParam);

        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (MemberLoginLogDto memberLoginLogDto : list) {
                memberLoginLogDto.setTotalCount(totalCount);

                // 테이블 NO 셋팅
                memberLoginLogDto.setSequential(totalCount - i);
                i++;
            }
        }
        return list;
    }
}