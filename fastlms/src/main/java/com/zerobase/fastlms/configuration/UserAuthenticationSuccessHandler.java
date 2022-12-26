package com.zerobase.fastlms.configuration;


import com.zerobase.fastlms.member.domain.MemberHistory;
import com.zerobase.fastlms.member.repository.MemberHistoryRepository;
import com.zerobase.fastlms.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final MemberRepository memberRepository;
    private final MemberHistoryRepository memberHistoryRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();
        LocalDateTime nowLocalDate = LocalDateTime.now();

        memberHistoryRepository.save(
                MemberHistory.builder()
                        .userId(authentication.getName())
                        .userAgent(userAgent)
                        .ip(ip)
                        .loginDt(nowLocalDate)
                        .build()
        );

        try {
            memberRepository.changeLoginDt(nowLocalDate, authentication.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
