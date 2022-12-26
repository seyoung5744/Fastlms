package com.zerobase.fastlms.member.controller;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.common.model.ResponseResult;
import com.zerobase.fastlms.course.dto.TakeCourseDto;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.model.TakeCourseInput;
import com.zerobase.fastlms.course.model.TakeCourseParam;
import com.zerobase.fastlms.course.service.TakeCourseService;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class ApiMemberController {
    private final MemberService memberService;
    private final TakeCourseService takeCourseService;

    // 데이터를 서버로 submit을 보내기 때문에 서버에서 받는 부분에서 post, get도 받아야함. RequestMapping으로 설정
    @PostMapping("/api/member/course/cancel.api")
    public ResponseEntity<?> cancelCourse(
            @RequestBody TakeCourseInput parameter,
            Principal principal
    ) {

        // 내 강좌인지 확인
        TakeCourseDto detail = takeCourseService.detail(parameter.getTakeCourseId());
        if (detail == null) {
            ResponseResult responseResult = new ResponseResult(false, "수강 신청 정보가 존재하지 않습니다.");
            return ResponseEntity.ok().body(responseResult);
        }

        String userId = principal.getName();

        if(userId == null || !userId.equals(detail.getUserId())){
            // 내 수강 신청 정보가 아님.
            ResponseResult responseResult = new ResponseResult(false, "본인의 수강 신청 정보만 취소할 수 있습니다.");
            return ResponseEntity.ok().body(responseResult);
        }

        ServiceResult serviceResult = takeCourseService.cancel(parameter.getTakeCourseId());

        if(!serviceResult.isResult()){
            // 내 수강 신청 정보가 아님.
            ResponseResult responseResult = new ResponseResult(false, serviceResult.getMessage());
            return ResponseEntity.ok().body(responseResult);
        }

        ResponseResult responseResult = new ResponseResult(true);
        return ResponseEntity.ok().body(responseResult);
    }
}
