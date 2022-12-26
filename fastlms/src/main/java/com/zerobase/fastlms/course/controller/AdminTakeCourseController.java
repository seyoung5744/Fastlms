package com.zerobase.fastlms.course.controller;

import com.zerobase.fastlms.admin.service.CategoryService;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.dto.TakeCourseDto;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.model.TakeCourseParam;
import com.zerobase.fastlms.course.service.CourseService;
import com.zerobase.fastlms.course.service.TakeCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminTakeCourseController extends BaseController {

    private final CourseService courseService;
    private final TakeCourseService takeCourseService;
    private final CategoryService categoryService;

    @GetMapping("/admin/takecourse/list.do")
    public String list(Model model, TakeCourseParam takeCourseParam,
                       BindingResult bindingResult
    ) {

        if(bindingResult.hasErrors()){
            System.out.println("++++++++++++++++++++++++++++++++++++++");
            System.out.println(bindingResult.getAllErrors());
        }

        // pageIndex, pageSize 초기화
        takeCourseParam.init();

        List<TakeCourseDto> takeCourseList = takeCourseService.list(takeCourseParam);

        long totalCount = 0;

        if (!CollectionUtils.isEmpty(takeCourseList)) {
            totalCount = takeCourseList.get(0).getTotalCount();
        }

        String queryString = takeCourseParam.getQueryString();

        String pagerHtml = getPagerHtml(totalCount, takeCourseParam.getPageSize(), takeCourseParam.getPageIndex(), queryString);

        System.out.println(takeCourseList + ", " + totalCount);
        model.addAttribute("takeCourseList", takeCourseList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        List<CourseDto> courseList = courseService.listAll();
        model.addAttribute("courseList", courseList);

        return "admin/takecourse/list";
    }

    @PostMapping("/admin/takecourse/status.do")
    public String status(Model model, TakeCourseParam takeCourseParam) {

        ServiceResult serviceResult = takeCourseService.updateStatus(
                takeCourseParam.getId(),
                takeCourseParam.getStatus()
        );

        if (!serviceResult.isResult()) {
            model.addAttribute("message", serviceResult.getMessage());
            return "common/error";
        }

        return "redirect:/admin/takecourse/list.do";
    }


}
