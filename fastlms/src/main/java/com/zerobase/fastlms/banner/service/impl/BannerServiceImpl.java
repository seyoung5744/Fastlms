package com.zerobase.fastlms.banner.service.impl;

import com.zerobase.fastlms.banner.domain.Banner;
import com.zerobase.fastlms.banner.dto.BannerDto;
import com.zerobase.fastlms.banner.mapper.BannerMapper;
import com.zerobase.fastlms.banner.model.BannerInput;
import com.zerobase.fastlms.banner.model.BannerParam;
import com.zerobase.fastlms.banner.repository.BannerRepository;
import com.zerobase.fastlms.banner.service.BannerService;
import com.zerobase.fastlms.course.domain.Course;
import com.zerobase.fastlms.course.dto.CourseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {
    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;

    @Override
    public boolean add(BannerInput bannerInput) {
        bannerRepository.save(
                Banner.builder()
                        .subject(bannerInput.getSubject())
                        .status(bannerInput.getStatus())
                        .openYn(bannerInput.isOpenYn())
                        .sequence(bannerInput.getSequence())
                        .registerDt(LocalDateTime.now())
                        .filename(bannerInput.getFilename())
                        .urlFilename(bannerInput.getUrlFilename())
                        .build()
        );
        return false;
    }

    @Override
    public boolean set(BannerInput bannerInput) {

        Optional<Banner> optionalBanner = bannerRepository.findById(bannerInput.getId());

        if (!optionalBanner.isPresent()) {
            // 수정할 데이터 없음
            return false;
        }

        Banner banner = optionalBanner.get();
        banner.setSubject(bannerInput.getSubject());
        banner.setStatus(bannerInput.getStatus());
        banner.setOpenYn(bannerInput.isOpenYn());
        banner.setSequence(bannerInput.getSequence());
        banner.setFilename(bannerInput.getFilename());
        banner.setUrlFilename(bannerInput.getUrlFilename());

        bannerRepository.save(banner);

        return true;
    }

    @Override
    public List<BannerDto> list(BannerParam bannerParam) {
        long totalCount = bannerMapper.selectListCount(bannerParam);

        List<BannerDto> list = bannerMapper.selectList(bannerParam);

        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (BannerDto bannerDto : list) {
                bannerDto.setTotalCount(totalCount);

                // 테이블 NO 셋팅
                bannerDto.setSequential(totalCount - bannerParam.getPageStart() - i);
                i++;
            }
        }
        return list;
    }

    @Override
    public BannerDto getById(long id) {
        return bannerRepository.findById(id).map(BannerDto::of).orElse(null);
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
                    bannerRepository.deleteById(id);
                }
            }
        }
        return false;
    }

    @Override
    public List<BannerDto> listAll() {
        Optional<List<Banner>> optionalBannerList = bannerRepository.findByOpenYnIsTrueOrderBySequenceAsc();

        if(optionalBannerList.isPresent()){
            return BannerDto.of(optionalBannerList.get());
        }

        return null;
    }

    @Override
    public boolean checkSequence(int sequence) {
        return bannerRepository.existsBySequence(sequence);
    }
}