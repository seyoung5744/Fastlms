package com.zerobase.fastlms.admin.dto;

import com.zerobase.fastlms.course.domain.Course;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.member.domain.Member;
import com.zerobase.fastlms.member.domain.MemberHistory;
import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberLoginLogDto {
    private Long id;

    private String userId;

    private String ip;
    private String userAgent;
    private LocalDateTime loginDt;

    // 필요에 의한 추가...원래 여기 잇으면 안돼
    private long totalCount;

    // 테이블 NO 셋팅
    private long sequential;

    public static MemberLoginLogDto of(MemberHistory memberHistory) {
        return MemberLoginLogDto.builder()
                .loginDt(memberHistory.getLoginDt())
                .ip(memberHistory.getIp())
                .userAgent(memberHistory.getUserAgent())
                .build();
    }

    public static List<MemberLoginLogDto> of(List<MemberHistory> memberHistoryList) {
        if(memberHistoryList == null){
            return null;
        }
        return memberHistoryList.stream().map(MemberLoginLogDto::of).collect(Collectors.toList());
    }

    public String getLoginDtText(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return loginDt != null ? this.loginDt.format(dateTimeFormatter) : "";
    }
}
