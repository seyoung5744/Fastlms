package com.zerobase.fastlms.admin.dto;

import com.zerobase.fastlms.admin.domain.Category;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;

    private String categoryName;

    // 카테고리 순서
    int sortValue;

    // 사용 여부
    boolean usingYn;

    // 카테고리별 등록된 강좌 수
    int courseCount;

    public static List<CategoryDto> of(List<Category> categoryList) {
//       if(categoryList != null){
//           List<CategoryDto> categoryDtoList = new ArrayList<>();
//           for(Category category : categoryList){
//                categoryDtoList.add(of(category));
//           }
//           return categoryDtoList;
//       }
//
//       return null;

        if (categoryList == null) {
            return null;
        }
        return categoryList.stream()
                .map(CategoryDto::of)
                .collect(Collectors.toList());
    }

    public static CategoryDto of(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .sortValue(category.getSortValue())
                .usingYn(category.isUsingYn())
                .build();
    }
}
