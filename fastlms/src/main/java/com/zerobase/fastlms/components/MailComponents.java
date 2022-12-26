package com.zerobase.fastlms.components;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class MailComponents {
    // 본래 메일 서버가 셋팅되어야 됨.
    // SMTP 라는 프로토콜을 이용해서 메일 전송
    // 시스템 구축 시. 도메인 구매 -> 도메인에 해당하는 메일 서버 구축

    private final JavaMailSender javaMailSender;

    public void sendMailTest() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo("seyoung5744@naver.com");
        simpleMailMessage.setSubject("안녕하세요. 제로베이스입니다.");
        simpleMailMessage.setText("안녕하세요. 제로베이스입니다. 반갑습니다.");

        javaMailSender.send(simpleMailMessage);
    }

    public boolean sendMail(String mail, String subject, String text) {
        MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                        mimeMessage, true, "UTF-8");

                mimeMessageHelper.setTo(mail);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(text, true); // html을 사용해서 메일 보내기
            }
        };

        try {
            javaMailSender.send(mimeMessagePreparator);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }
}
