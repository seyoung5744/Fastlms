package com.zerobase.fastlms.admin.service;

import com.zerobase.fastlms.admin.domain.Category;
import com.zerobase.fastlms.admin.dto.CategoryDto;
import com.zerobase.fastlms.admin.model.CategoryInput;

import java.util.List;

public interface CategoryService {
    /**
     *
     */
    List<CategoryDto> list();

    /**
     * 카테고리 신규 추가
     */
    boolean add(String categoryName);

    /**
     * 카테고리 수정
     */
    boolean update(CategoryInput categoryInput);

    /**
     * 카테고리 삭제
     */
    boolean delete(long id);


    /**
     * 프론트에서 카테고리별 강좌 목록
     */
    List<CategoryDto> frontList(CategoryDto categoryDto);
}
