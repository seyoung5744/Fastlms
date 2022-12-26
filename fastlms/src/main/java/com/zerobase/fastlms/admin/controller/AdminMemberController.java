package com.zerobase.fastlms.admin.controller;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.admin.dto.MemberLoginLogDto;
import com.zerobase.fastlms.admin.model.HistoryParam;
import com.zerobase.fastlms.admin.model.MemberInfo;
import com.zerobase.fastlms.admin.model.MemberParam;
import com.zerobase.fastlms.course.controller.BaseController;
import com.zerobase.fastlms.member.service.HistoryService;
import com.zerobase.fastlms.member.service.MemberService;
import com.zerobase.fastlms.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminMemberController extends BaseController {
    private final MemberService memberService;
    private final HistoryService historyService;

    @GetMapping("/admin/member/list.do")
    public String list(Model model, MemberParam memberParam) {

        // pageIndex, pageSize 초기화
        memberParam.init();

        List<MemberDto> memberList = memberService.list(memberParam);

        long totalCount = 0;
        if(!CollectionUtils.isEmpty(memberList)){
            totalCount = memberList.get(0).getTotalCount();
        }

        String queryString = memberParam.getQueryString();

        String pagerHtml = getPagerHtml(totalCount, memberParam.getPageSize(), memberParam.getPageIndex(), queryString);

        model.addAttribute("memberList", memberList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/member/list";
    }

    @GetMapping("/admin/member/detail.do")
    public String detail(Model model, MemberParam memberParam, HistoryParam historyParam) {

        // pageIndex, pageSize 초기화
        memberParam.init();

        MemberDto memberDto = memberService.detail(memberParam.getUserId());
        model.addAttribute("member", memberDto);

        List<MemberLoginLogDto> memberLoginLogList = historyService.list(historyParam);


        model.addAttribute("memberLoginLogList", memberLoginLogList);

        return "admin/member/detail";
    }

    @PostMapping("/admin/member/status.do")
    public String status(Model model, MemberInfo memberInfo) {
        memberService.updateStatus(
                memberInfo.getUserId(),
                memberInfo.getUserStatus());

        return "redirect:/admin/member/detail.do?userId=" + memberInfo.getUserId();
    }

    @PostMapping("/admin/member/password.do")
    public String password(Model model, MemberInfo memberInfo) {
        memberService.updatePassword(
                memberInfo.getUserId(),
                memberInfo.getPassword());

        return "redirect:/admin/member/detail.do?userId=" + memberInfo.getUserId();
    }
}
