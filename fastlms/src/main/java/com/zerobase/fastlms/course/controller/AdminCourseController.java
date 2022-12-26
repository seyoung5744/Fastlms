package com.zerobase.fastlms.course.controller;

import com.zerobase.fastlms.admin.service.CategoryService;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminCourseController extends BaseController {

    private final CourseService courseService;
    private final CategoryService categoryService;

    @GetMapping("/admin/course/list.do")
    public String list(Model model, CourseParam courseParam) {

        // pageIndex, pageSize 초기화
        courseParam.init();

        List<CourseDto> courseList = courseService.list(courseParam);

        long totalCount = 0;

        if (!CollectionUtils.isEmpty(courseList)) {
            totalCount = courseList.get(0).getTotalCount();
        }

        String queryString = courseParam.getQueryString();

        String pagerHtml = getPagerHtml(totalCount, courseParam.getPageSize(), courseParam.getPageIndex(), queryString);

        model.addAttribute("courseList", courseList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/course/list";
    }

    @GetMapping(value = {"/admin/course/add.do", "/admin/course/edit.do"})
    public String add(Model model, HttpServletRequest request,
                      CourseInput courseInput) {

        // 카테고리 정보를 내려줘야 함.
        model.addAttribute("category", categoryService.list());

        boolean isEditMode = request.getRequestURI().contains("/edit.do");
        CourseDto courseDto = new CourseDto();

        if (isEditMode) { // 편집 모드일 때
            long id = courseInput.getId();

            CourseDto existCourse = courseService.getById(id);
            if (existCourse == null) {
                // error 처리
                model.addAttribute("message", "강좌 정보가 존재하지 않습니다.");
                return "common/error";
            }
            courseDto = existCourse;
        }

        model.addAttribute("editMode", isEditMode);
        model.addAttribute("detail", courseDto);
        return "admin/course/add";
    }


    private String[] getNewSaveFile(String baseLocalPath, String baseUrlPath, String originalFilename) {

        LocalDate now = LocalDate.now();

        String[] dirs = {
                String.format("%s\\%d", baseLocalPath, now.getYear()),
                String.format("%s\\%d\\%02d\\", baseLocalPath, now.getYear(), now.getMonthValue()),
                String.format("%s\\%d\\%02d\\%02d\\", baseLocalPath, now.getYear(), now.getMonthValue(), now.getDayOfMonth())
        };

        String urlDir = String.format("%s\\%d\\%02d\\%02d\\", baseUrlPath, now.getYear(), now.getMonthValue(), now.getDayOfMonth());

        for (String dir : dirs) {
            File file = new File(dir);
            if (!file.isDirectory()) {
                file.mkdirs();
            }
        }

        String fileExtension = "";

        if (originalFilename != null) {
            int dotPos = originalFilename.lastIndexOf(".");
            if (dotPos > -1) {
                fileExtension = originalFilename.substring(dotPos + 1);
            }
        }

        // 새로운 파일명(중복안되게)
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        String newFilename = String.format("%s%s", dirs[2], uuid);
        String newUrlFilename = String.format("%s%s", urlDir, uuid);

        if (fileExtension.length() > 0) {
            newFilename += "." + fileExtension;
            newUrlFilename += "." + fileExtension;
        }

        return new String[]{newFilename, newUrlFilename};
    }

    @PostMapping(value = {"/admin/course/add.do", "/admin/course/edit.do"})
    public String addSubmit(Model model,
                            HttpServletRequest request,
                            @RequestParam(value = "file", required = false) MultipartFile multipartFile,
                            CourseInput courseInput
    ) {

        String saveFilename = "";
        String urlFilename = "";

        if (multipartFile != null) {
            String originalFilename = multipartFile.getOriginalFilename();
            String baseLocalPath = "C:\\Users\\seyou\\Desktop\\my_Git_Hub\\zero-base-java\\Part2\\Part08. Spring Boot\\fastlms\\files";
            String baseUrlPath = "\\files";

            String[] arrFilename = getNewSaveFile(baseLocalPath, baseUrlPath, originalFilename);

            saveFilename = arrFilename[0];
            urlFilename = arrFilename[1];

            try {
                File newFile = new File(saveFilename);
                FileCopyUtils.copy(multipartFile.getInputStream(), Files.newOutputStream(Paths.get(newFile.getPath())));
            } catch (IOException e) {
                log.error("##########################");
                log.error(e.getMessage());
            }
        }

        courseInput.setFilename(saveFilename);
        courseInput.setUrlFilename(urlFilename);

        boolean isEditMode = request.getRequestURI().contains("/edit.do");

        if (isEditMode) { // 편집 모드일 때
            long id = courseInput.getId();

            CourseDto existCourse = courseService.getById(id);
            if (existCourse == null) {
                // error 처리
                model.addAttribute("message", "강좌 정보가 존재하지 않습니다.");
                return "common/error";
            }

            boolean result = courseService.set(courseInput);
        } else {
            boolean result = courseService.add(courseInput);
        }

        return "redirect:/admin/course/list.do";
    }


    @PostMapping("/admin/course/delete.do")
    public String delete(Model model, CourseInput courseInput, HttpServletRequest request) {

        boolean result = courseService.delete(courseInput.getIdList());

        return "redirect:/admin/course/list.do";
    }
}
