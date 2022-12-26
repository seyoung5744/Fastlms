package com.zerobase.fastlms.configuration;

import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity // https://velog.io/@seongwon97/security
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    // authenticationManagerBuiler라고 해서 기본적으로 user 정보를 넘겨주는 부분
    private final MemberService memberService;
    private final UserAuthenticationSuccessHandler userAuthenticationSuccessHandler;

    @Bean
    PasswordEncoder getPasswordEncoder() {
        // BCryptPasswordEncoder로 로그인 시 인코딩되므로
        // 회원가입 할때도 동일한 인코딩 방식을 사용해야 함.
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserAuthenticationFailureHandler getFailureHandler() {
        return new UserAuthenticationFailureHandler();
    }

//    @Bean
//    UserAuthenticationSuccessHandler getSuccessHandler() {
//        System.out.println("얻어??");
//        return new UserAuthenticationSuccessHandler();
//    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        // 스프링에서 허용하지 않는 경로 무시
        web.ignoring().antMatchers("/favicon.ico", "/files/**");

        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        // smartEditor은 내부적으로 IFrame 설정을 사용
        // domain이 다름에 따른 권한 에러
        http.headers().frameOptions().sameOrigin();

        http.authorizeRequests((user) -> user
                        .antMatchers(
                                "/",
                                "/member/register",
                                "/member/email-auth",
                                "/member/find/password",
                                "/member/reset/password"
                        )
                        .permitAll()
                )
                .authorizeRequests((admin) -> admin
                        // 관리자 페이지 접근시 ROLE_ADMIN 이 있어야 접근이 가능
                        .antMatchers("/admin/**")
                        .hasAuthority("ROLE_ADMIN")
                );

        http.formLogin((login) -> login
                        .loginPage("/member/login")
                        .successHandler(userAuthenticationSuccessHandler)
                        .failureHandler(getFailureHandler()) // 로그인 실패 시 handler
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 루트로
                        .invalidateHttpSession(true) // 세션 초기화
                );

        // 에러 상황에선 /error/denied 페이지로 이동
        http.exceptionHandling()
                .accessDeniedPage("/error/denied");


        super.configure(http);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Authentication Configuration 할때 MemberService가 갖고 있는 interface가 잇어야 함.

        // user를 관리하는 service가 무엇이고
        // 그거에 해당하는 비밀번호 encoder가 무엇인지 설정.
        auth.userDetailsService(memberService)
                .passwordEncoder(getPasswordEncoder());


        super.configure(auth);
    }
}
