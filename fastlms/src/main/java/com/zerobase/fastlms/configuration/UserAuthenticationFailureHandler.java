package com.zerobase.fastlms.configuration;


import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        String msg = "로그인에 실패하였습니다.";

        if(exception instanceof InternalAuthenticationServiceException){
            msg = exception.getMessage();
        }

        setUseForward(true);
        setDefaultFailureUrl("/member/login?error=true"); // error 메시지 전송
        request.setAttribute("errorMessage",msg); // 프론트에 데이터 전달

        System.out.println("로그인에 실패하였습니다.");
        super.onAuthenticationFailure(request, response, exception);
    }
}
