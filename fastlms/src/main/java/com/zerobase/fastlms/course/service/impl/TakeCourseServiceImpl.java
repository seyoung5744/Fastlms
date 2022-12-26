package com.zerobase.fastlms.course.service.impl;

import com.zerobase.fastlms.course.domain.TakeCourse;
import com.zerobase.fastlms.course.domain.TakeCourseCode;
import com.zerobase.fastlms.course.dto.TakeCourseDto;
import com.zerobase.fastlms.course.mapper.TakeCourseMapper;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.model.TakeCourseParam;
import com.zerobase.fastlms.course.repository.TakeCourseRepository;
import com.zerobase.fastlms.course.service.TakeCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TakeCourseServiceImpl implements TakeCourseService {
    private final TakeCourseRepository takeCourseRepository;
    private final TakeCourseMapper takeCourseMapper;

    @Override
    public List<TakeCourseDto> list(TakeCourseParam takeCourseParam) {
        long totalCount = takeCourseMapper.selectListCount(takeCourseParam);

        List<TakeCourseDto> list = takeCourseMapper.selectList(takeCourseParam);

        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (TakeCourseDto memberDto : list) {
                memberDto.setTotalCount(totalCount);

                // 테이블 NO 셋팅
                memberDto.setSequential(totalCount - takeCourseParam.getPageStart() - i);
                i++;
            }
        }
        return list;
    }

    @Override
    public TakeCourseDto detail(long id) {
        return takeCourseRepository.findById(id).map(TakeCourseDto::of).orElse(null);
    }

    @Override
    public ServiceResult updateStatus(Long id, String status) {
        Optional<TakeCourse> optionalTakeCourse = takeCourseRepository.findById(id);
        if (!optionalTakeCourse.isPresent()) {
            return new ServiceResult(false, "수강 정보가 존재하지 않습니다.");
        }

        TakeCourse takeCourse = optionalTakeCourse.get();

        takeCourse.setStatus(status);
        takeCourseRepository.save(takeCourse);

        return new ServiceResult(true);
    }

    @Override
    public List<TakeCourseDto> myCourse(String userId) {
        TakeCourseParam takeCourseParam = new TakeCourseParam();
        takeCourseParam.setUserId(userId);

        return takeCourseMapper.selectListMyCourse(takeCourseParam);
    }

    @Override
    public ServiceResult cancel(Long id) {
        Optional<TakeCourse> optionalTakeCourse = takeCourseRepository.findById(id);
        if (!optionalTakeCourse.isPresent()) {
            return new ServiceResult(false, "수강 정보가 존재하지 않습니다.");
        }

        TakeCourse takeCourse = optionalTakeCourse.get();

        takeCourse.setStatus(TakeCourseCode.STATUS_CANCEL);
        takeCourseRepository.save(takeCourse);

        return new ServiceResult(true);
    }
}
