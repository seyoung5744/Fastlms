package com.zerobase.fastlms.member.controller;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.course.dto.TakeCourseDto;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.service.TakeCourseService;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import com.zerobase.fastlms.member.service.HistoryService;
import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final HistoryService historyService;
    private final TakeCourseService takeCourseService;

    // 데이터를 서버로 submit을 보내기 때문에 서버에서 받는 부분에서 post, get도 받아야함. RequestMapping으로 설정
    @RequestMapping("/member/login")
    public String login(HttpServletRequest request) {

        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();


        return "member/login";
    }

    @GetMapping("/member/find/password")
    public String findPassword() {

        return "member/find_password";
    }

    // button submit으로 데이터를 받기 때문에 post 필요
    @PostMapping("/member/find/password")
    public String findPasswordSubmit(
            Model model,
            ResetPasswordInput resetPasswordInput
    ) {
        boolean result = false;
        try {
            result = memberService.sendResetPassword(resetPasswordInput);
        } catch (Exception e) {

        }
        model.addAttribute("result", result);

        // 단순히 /(루트)만 반환하면 주소는 그대로인데 View만 바뀌는 현상 발생
        // 그래서 주소와 같이 View도 바뀌게 하려면 redirect: 이용
        // return "redirect:/ ";

        // 강의에선 정책 시나리오 변경으로 인해
        // 비밀번호 초기화 이메일 발송이 성공했다는 결과를 보여주는 것으로 변경
        return "member/find_password_result";
    }

    @GetMapping("/member/reset/password")
    public String resetPassword(Model model, HttpServletRequest request) {
        String uuid = request.getParameter("id");
        model.addAttribute("uuid", uuid);

        // uuid 체크를 통해 비밀번호 초기화가 가능한지 판별
        boolean result = memberService.checkResetPassword(uuid);
        model.addAttribute("result", result);

        return "member/reset_password";
    }

    @PostMapping("/member/reset/password")
    public String resetPasswordSubmit(Model model, ResetPasswordInput resetPasswordInput) {
        boolean result = false;
        try {
            result = memberService.resetPassword(
                    resetPasswordInput.getId(),
                    resetPasswordInput.getPassword()
            );
        } catch (Exception e) {
        }

        model.addAttribute("result", result);


        return "member/reset_password_result";
    }


    //    @RequestMapping(value = "/member/register", method = RequestMethod.GET)
    @GetMapping("/member/register")
    public String register() {
        return "member/register";
    }

    // request WEB -> Server
    // response Server -> WEB
    @PostMapping("/member/register")
    public String registerSubmit(Model model, // 클라이언트한테 데이터를 전달하기 위해서 사용되는 인터페이스
                                 HttpServletRequest request,
                                 MemberInput memberInput) {
        boolean result = memberService.register(memberInput);

        // Model을 사용하여 result 값을 register_complete.html에서 활용 가능
        model.addAttribute("result", result);

        return "member/register_complete";
    }

    // http://www.naver.com/news/list.do?id=123
    // http://
    // 프로토콜://도메일(IP)/서버주소
    // ex) http://www.nver.com/cafe/
    // ? 뒤로는 parameter, query string이라고 한다.


    /**
     * 계정 활성화 확인 페이지
     */
    @GetMapping("/member/email-auth")
    public String emailAuth(Model model, HttpServletRequest request) {
        String uuid = request.getParameter("id");
        System.out.println(uuid);

        boolean isEmailAuth = memberService.emailAuth(uuid);
        model.addAttribute("isEmailAuth", isEmailAuth);

        return "member/email_auth";
    }

    @GetMapping("/member/info")
    public String memberInfo(Model model, Principal principal) {

        // Spring Security 에 의해 저장된 유저 id
        String userId = principal.getName();

        MemberDto detail = memberService.detail(userId);

        model.addAttribute("detail", detail);

        return "member/info";
    }

    @PostMapping("/member/info")
    public String memberInfoSubmit(Model model
            , MemberInput memberInput
            , Principal principal
    ) {

        // Spring Security 에 의해 저장된 유저 id
        String userId = principal.getName();
        memberInput.setUserId(userId);

        ServiceResult serviceResult = memberService.updateMember(memberInput);
        if (!serviceResult.isResult()) {
            model.addAttribute("message", serviceResult.getMessage());
            return "common/error";
        }


        return "redirect:/member/info";
    }

    @GetMapping("/member/password")
    public String memberPassword(Model model, Principal principal) {

        // Spring Security 에 의해 저장된 유저 id
        String userId = principal.getName();

        MemberDto detail = memberService.detail(userId);

        model.addAttribute("detail", detail);

        return "member/password";
    }

    @PostMapping("/member/password")
    public String memberPasswordSubmit(
            Model model,
            MemberInput memberInput,
            Principal principal
    ) {

        // Spring Security 에 의해 저장된 유저 id
        String userId = principal.getName();

        memberInput.setUserId(userId);
        ServiceResult serviceResult = memberService.updateMemberPassword(memberInput);
        if (!serviceResult.isResult()) {
            model.addAttribute("message", serviceResult.getMessage());
            return "common/error";
        }

        return "redirect:/member/info";
    }

    @GetMapping("/member/takecourse")
    public String memberTakeCourse(Model model, Principal principal) {

        // Spring Security 에 의해 저장된 유저 id
        String userId = principal.getName();
        MemberDto detail = memberService.detail(userId);

        List<TakeCourseDto> takeCourseList = takeCourseService.myCourse(userId);
        model.addAttribute("takeCourseList", takeCourseList);

        return "member/takecourse";
    }

    @GetMapping("/member/withdraw")
    public String memberWithdraw(Model model) {

        return "member/withdraw";
    }

    @PostMapping("/member/withdraw")
    public String memberWithdrawSubmit(
            Model model,
            MemberInput parameter,
            Principal principal
    ) {
        String userId = principal.getName();

        ServiceResult serviceResult = memberService.withdraw(userId, parameter.getPassword());

        if (!serviceResult.isResult()) {
            model.addAttribute("message", serviceResult.getMessage());
            return "common/error";
        }

        return "redirect:/member/logout";
    }

}
