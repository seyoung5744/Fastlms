package com.zerobase.fastlms.main.controller;

import com.zerobase.fastlms.banner.domain.Banner;
import com.zerobase.fastlms.banner.dto.BannerDto;
import com.zerobase.fastlms.banner.repository.BannerRepository;
import com.zerobase.fastlms.banner.service.BannerService;
import com.zerobase.fastlms.components.MailComponents;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.util.RequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

// MainPage 클래스
// 매핑하기 위해서...
// 주소와(논리적인 인터넷 주소) 물리적인 파일 매핑

// https://www.naver.com/new/list.do
// 하나의 주소에 대해서 어디서 매핑?? 누가?
// 후보군? 클래스 , 속성, 메소드
// 만약 클래스라면 주소가 추가될 때마다 클래스가 추가되야함. -> 약간 비효율적
// 속성? 속성은 값이므로 부적절
// 메소드를 통해서 매핑하겠구나~


//@RestController // Mapping 하기 위한 Controller라는 것을 Spring한테 알려줘야함.
@Controller
@RequiredArgsConstructor
public class MainController {

    private final MailComponents mailComponents;
    private final BannerService bannerService;

    @RequestMapping("/")
    public String index(Model model){
//        String email = "seyoung5744@naver.com";
//        String subject = "안녕하세요. 제로베이스입니다.";
//        String text = "<p>안녕하세요</p><p>반갑습니다.</p>";

//        mailComponents.sendMail(email, subject, text);

        List<BannerDto> bannerDtoList = bannerService.listAll();
        System.out.println(bannerDtoList);
        model.addAttribute("bannerDtoList", bannerDtoList);
        return "index";
    }

    @RequestMapping("/error/denied")
    public String errorDenied(){
        return "error/denied";
    }


    // 스프링 -> MVC (View -> 템플릿 엔진 화면에 내용을 출력(html))
    // .NET -> MVC (View -> 출력)
    // python django -> MVT(Template -> 화면출력)

    // V -> Html로 바인딩 ==> 웹피이지
    // V -> json로 바인딩 ==> API

    // request : Web -> Server => HttpServelRequest
    // response: Server -> Web => HttpServletResponse

    @RequestMapping("/hello")
    public void hello(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter printWriter = response.getWriter();

        String msg = "<html>" +
                    "<head>" +
                    "<meta charset=\"UTF-8\">" +
                    "</head>" +
                    "<body>" +
                    "<p>hello</p>" +
                    "<p>fastlms website!!</p>" +
                    "<p>안녕하세요</p>"+
                    "</body>" +
                    "</html>";

        printWriter.write(msg);
        printWriter.close();
    }
}
