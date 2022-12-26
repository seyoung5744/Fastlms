package com.zerobase.fastlms.banner.controller;

import com.zerobase.fastlms.banner.dto.BannerDto;
import com.zerobase.fastlms.banner.model.BannerInput;
import com.zerobase.fastlms.banner.model.BannerParam;
import com.zerobase.fastlms.banner.service.BannerService;
import com.zerobase.fastlms.course.controller.BaseController;
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
public class AdminBannerController extends BaseController {

    private final BannerService bannerService;

    @GetMapping("/admin/banner/list.do")
    public String list(Model model, BannerParam bannerParam) {

        // pageIndex, pageSize 초기화
        bannerParam.init();

        List<BannerDto> bannerList = bannerService.list(bannerParam);

        long totalCount = 0;

        if (!CollectionUtils.isEmpty(bannerList)) {
            totalCount = bannerList.get(0).getTotalCount();
        }

        String queryString = bannerParam.getQueryString();

        String pagerHtml = getPagerHtml(totalCount, bannerParam.getPageSize(), bannerParam.getPageIndex(), queryString);

        model.addAttribute("bannerList", bannerList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/banner/list";
    }

    @GetMapping(value = {"/admin/banner/add.do", "/admin/banner/edit.do"})
    public String add(Model model, HttpServletRequest request,
                      BannerInput BannerInput) {

        boolean isEditMode = request.getRequestURI().contains("/edit.do");
        BannerDto bannerDto = new BannerDto();

        if (isEditMode) { // 편집 모드일 때
            long id = BannerInput.getId();

            BannerDto existCourse = bannerService.getById(id);
            if (existCourse == null) {
                // error 처리
                model.addAttribute("message", "강좌 정보가 존재하지 않습니다.");
                return "common/error";
            }
            bannerDto = existCourse;
        }

        model.addAttribute("editMode", isEditMode);
        model.addAttribute("detail", bannerDto);
        return "admin/banner/add";
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

    @PostMapping(value = {"/admin/banner/add.do", "/admin/banner/edit.do"})
    public String addSubmit(Model model,
                            HttpServletRequest request,
                            @RequestParam(value = "file", required = false) MultipartFile multipartFile,
                            BannerInput bannerInput
    ) {

        String saveFilename = "";
        String urlFilename = "";


        if (multipartFile != null) {
            String originalFilename = multipartFile.getOriginalFilename();


            String baseLocalPath = "C:\\Users\\seyou\\Desktop\\my_Git_Hub\\Fastlms\\fastlms\\files";
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

        bannerInput.setFilename(saveFilename);
        bannerInput.setUrlFilename(urlFilename);

        boolean isEditMode = request.getRequestURI().contains("/edit.do");

        if (isEditMode) { // 편집 모드일 때
            long id = bannerInput.getId();

            BannerDto existCourse = bannerService.getById(id);
            if (existCourse == null) {
                // error 처리
                model.addAttribute("message", "강좌 정보가 존재하지 않습니다.");
                return "common/error";
            }

            boolean result = bannerService.set(bannerInput);
        } else {
            boolean result = bannerService.add(bannerInput);
        }

        return "redirect:/admin/banner/list.do";
    }


    @PostMapping("/admin/banner/delete.do")
    public String delete(Model model, BannerInput bannerInput, HttpServletRequest request) {

        boolean result = bannerService.delete(bannerInput.getIdList());

        return "redirect:/admin/banner/list.do";
    }
}
