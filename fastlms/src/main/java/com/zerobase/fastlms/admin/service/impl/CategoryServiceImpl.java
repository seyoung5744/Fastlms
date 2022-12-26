package com.zerobase.fastlms.admin.service.impl;

import com.zerobase.fastlms.admin.domain.Category;
import com.zerobase.fastlms.admin.dto.CategoryDto;
import com.zerobase.fastlms.admin.mapper.CategoryMapper;
import com.zerobase.fastlms.admin.model.CategoryInput;
import com.zerobase.fastlms.admin.repository.CategoryRepository;
import com.zerobase.fastlms.admin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> list() {
        return categoryRepository.findAllByOrderBySortValueDesc()
                .map(CategoryDto::of).orElse(null);
    }

    /**
     * 카테고리 신규 추가
     */
    @Override
    public boolean add(String categoryName) {
        // 카테고리명 중복 체크

        Category category = Category.builder()
                .categoryName(categoryName)
                .usingYn(true)
                .sortValue(0)
                .build();

        categoryRepository.save(category);

        return true;
    }

    /**
     * 카테고리 수정
     */
    @Override
    public boolean update(CategoryInput categoryInput) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryInput.getId());

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setCategoryName(categoryInput.getCategoryName());
            category.setSortValue(categoryInput.getSortValue());
            category.setUsingYn(categoryInput.isUsingYn());

            categoryRepository.save(category);
        }
        return true;
    }

    /**
     * 카테고리 삭제
     */
    @Override
    public boolean delete(long id) {
        categoryRepository.deleteById(id);
        return true;
    }

    @Override
    public List<CategoryDto> frontList(CategoryDto categoryDto) {
        return categoryMapper.select(categoryDto);
    }


}
