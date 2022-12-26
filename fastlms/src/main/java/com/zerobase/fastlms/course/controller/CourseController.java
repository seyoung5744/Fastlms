package com.zerobase.fastlms.course.controller;

import com.zerobase.fastlms.admin.dto.CategoryDto;
import com.zerobase.fastlms.admin.service.CategoryService;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CourseController extends BaseController {

    private final CourseService courseService;
    private final CategoryService categoryService;

    @GetMapping("/course")
    public String course(Model model,
                         CourseParam courseParam
//                         ,@RequestParam(name="categoryId") long categoryId
    ) {

        // 전체 카테고리에 대한 강의 리스트
        List<CourseDto> courseList = courseService.frontList(courseParam);
        model.addAttribute("courseList", courseList);

        // using_yn==1 인 카테고리 리스트
        List<CategoryDto> categoryList = categoryService.frontList(CategoryDto.builder().build());
        int courseTotalCount = 0;
        if (categoryList != null) {
            for (CategoryDto x : categoryList) {
                courseTotalCount += x.getCourseCount();
            }
        }

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("courseTotalCount", courseTotalCount);

        return "course/index";
    }

    @GetMapping("/course/{id}")
    public String courseDetail(Model model,
                               CourseParam courseParam
    ) {

        CourseDto courseDetail = courseService.frontDetail(courseParam.getId());
        model.addAttribute("courseDetail", courseDetail);

        return "course/detail";
    }
}
