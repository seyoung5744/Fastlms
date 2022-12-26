package com.zerobase.fastlms.admin.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoryName;

    // 카테고리 순서
    int sortValue;

    // 사용 여부
    boolean usingYn;

}
