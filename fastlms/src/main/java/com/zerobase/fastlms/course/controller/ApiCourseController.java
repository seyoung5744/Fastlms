package com.zerobase.fastlms.course.controller;

import com.zerobase.fastlms.admin.service.CategoryService;
import com.zerobase.fastlms.common.model.ResponseResult;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.model.TakeCourseInput;
import com.zerobase.fastlms.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ApiCourseController extends BaseController {

    private final CourseService courseService;
    private final CategoryService categoryService;

    @PostMapping("/api/course/register.api")
    public ResponseEntity<?> courseRegister(Model model
            , @RequestBody TakeCourseInput takeCourseInput
            , Principal principal // spring security가 로그인 정보를 주입해줌.
    ) {

        takeCourseInput.setUserId(principal.getName()); // 로그인한 아이디

        ServiceResult serviceResult = courseService.register(takeCourseInput);

        if(!serviceResult.isResult()){
            ResponseResult responseResult = new ResponseResult(false, serviceResult.getMessage());
            return ResponseEntity.ok().body(responseResult);
        }

        ResponseResult responseResult = new ResponseResult(true);
        return ResponseEntity.ok().body(responseResult);
    }
}
