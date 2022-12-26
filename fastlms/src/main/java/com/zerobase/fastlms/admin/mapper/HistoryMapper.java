package com.zerobase.fastlms.admin.mapper;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.admin.dto.MemberLoginLogDto;
import com.zerobase.fastlms.admin.model.HistoryParam;
import com.zerobase.fastlms.admin.model.MemberParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

// resource/mybatis/MemberMapper.xml과 Mapping
// 실제 실행되는 부분은 MemberMapper.xml의 SQL문이 실행되서 resultType에 따른 결과 반환
@Mapper
public interface HistoryMapper {
    long selectListCount(HistoryParam historyParam);
    List<MemberLoginLogDto> selectList(HistoryParam historyParam);
}
