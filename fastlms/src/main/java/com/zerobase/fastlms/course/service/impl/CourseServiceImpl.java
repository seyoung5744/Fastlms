package com.zerobase.fastlms.course.service.impl;

import com.zerobase.fastlms.course.domain.Course;
import com.zerobase.fastlms.course.domain.TakeCourse;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.mapper.CourseMapper;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.model.TakeCourseInput;
import com.zerobase.fastlms.course.repository.CourseRepository;
import com.zerobase.fastlms.course.repository.TakeCourseRepository;
import com.zerobase.fastlms.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final TakeCourseRepository takeCourseRepository;

    private final CourseMapper courseMapper;

    private LocalDate getLocalDate(String value) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            return LocalDate.parse(value, dateTimeFormatter);
        } catch (Exception e) {
        }

        return null;
    }

    @Override
    public boolean add(CourseInput courseInput) {

        //2021-08-06
        //20210806
        LocalDate saleEndDt = getLocalDate(courseInput.getSaleEndDtText());

        courseRepository.save(
                Course.builder()
                        .categoryId(courseInput.getCategoryId())
                        .subject(courseInput.getSubject())
                        .keyword(courseInput.getKeyword())
                        .summary(courseInput.getSummary())
                        .contents(courseInput.getContents())
                        .price(courseInput.getPrice())
                        .salePrice(courseInput.getSalePrice())
                        // TODO : 종료일 문자열(문자열 -> LocalDateTime)
                        .saleEndDt(saleEndDt)
                        .registerDt(LocalDateTime.now())
                        .filename(courseInput.getFilename())
                        .urlFilename(courseInput.getUrlFilename())
                        .build()
        );
        return false;
    }

    @Override
    public boolean set(CourseInput courseInput) {
        LocalDate saleEndDt = getLocalDate(courseInput.getSaleEndDtText());

        Optional<Course> optionalCourse = courseRepository.findById(courseInput.getId());

        if (!optionalCourse.isPresent()) {
            // 수정할 데이터 없음
            return false;
        }

        Course course = optionalCourse.get();

        course.setCategoryId(courseInput.getCategoryId());
        course.setSubject(courseInput.getSubject());
        course.setKeyword(courseInput.getKeyword());
        course.setSummary(courseInput.getSummary());
        course.setContents(courseInput.getContents());
        course.setPrice(courseInput.getPrice());
        course.setSalePrice(courseInput.getSalePrice());

        // TODO : 종료일 문자열(문자열 -> LocalDateTime)
        course.setSaleEndDt(saleEndDt);
        course.setUpdateDt(LocalDateTime.now());

        course.setFilename(courseInput.getFilename());
        course.setUrlFilename(courseInput.getUrlFilename());

        courseRepository.save(course);
        return true;
    }

    @Override
    public List<CourseDto> list(CourseParam courseParam) {
        long totalCount = courseMapper.selectListCount(courseParam);

        List<CourseDto> list = courseMapper.selectList(courseParam);

        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (CourseDto memberDto : list) {
                memberDto.setTotalCount(totalCount);

                // 테이블 NO 셋팅
                memberDto.setSequential(totalCount - courseParam.getPageStart() - i);
                i++;
            }
        }
        return list;
    }

    @Override
    public CourseDto getById(long id) {
        return courseRepository.findById(id).map(CourseDto::of).orElse(null);

    }

    @Override
    public boolean delete(String idList) {
        if (idList != null && idList.length() > 0) {
            String[] ids = idList.split(",");
            for (String x : ids) {
                long id = 0L;
                try {
                    id = Long.parseLong(x);
                } catch (NumberFormatException e) {
                }

                if (id > 0) {
                    courseRepository.deleteById(id);
                }
            }
        }
        return true;
    }

    @Override
    public List<CourseDto> frontList(CourseParam courseParam) {
        System.out.println(courseParam.getCategoryId());
        if (courseParam.getCategoryId() < 1) {
            List<Course> courseList = courseRepository.findAll();
            return CourseDto.of(courseList);
        }

        // 카테고리 id에 등록된 강좌 목록
        Optional<List<Course>> optionalCourseList = courseRepository.findByCategoryId(courseParam.getCategoryId());
        if (optionalCourseList.isPresent()) {
            return CourseDto.of(optionalCourseList.get());
        }
        return null;

//        return courseRepository.findByCategoryId(courseParam.getCategoryId()).map(CourseDto::of).orElse(null);

    }

    @Override
    public CourseDto frontDetail(long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            return CourseDto.of(optionalCourse.get());
        }

        return null;
    }


    @Override
    public ServiceResult register(TakeCourseInput takeCourseInput) {

        ServiceResult result = new ServiceResult();

        Optional<Course> optionalCourse = courseRepository.findById(takeCourseInput.getCourseId());

        if (!optionalCourse.isPresent()) {

            result.setResult(false);
            result.setMessage("강좌 정보가 존재하지 않습니다.");
            return result;
        }
        Course course = optionalCourse.get();

        // 이미 신청 정보가 있는지 확인
        String[] statusList = {TakeCourse.STATUS_REQ, TakeCourse.STATUS_COMPLETE};
        long count = takeCourseRepository.countByCourseIdAndUserIdAndStatusIn(
                course.getId(),
                takeCourseInput.getUserId(),
                Arrays.asList(statusList)
        );

        if (count > 0) {
            result.setResult(false);
            result.setMessage("이미 신청한 강좌 정보가 존재합니다.");
            return result;
        }

        takeCourseRepository.save(
                TakeCourse.builder()
                        .courseId(course.getId())
                        .userId(takeCourseInput.getUserId())
                        .payPrice(course.getSalePrice())
                        .registerDt(LocalDateTime.now())
                        .status(TakeCourse.STATUS_REQ)
                        .build()
        );

        result.setResult(true);
        result.setMessage("");

        return result;
    }

    @Override
    public List<CourseDto> listAll() {
        List<Course> courseList = courseRepository.findAll();

        return CourseDto.of(courseList);
    }


}
