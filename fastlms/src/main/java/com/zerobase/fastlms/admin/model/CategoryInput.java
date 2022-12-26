package com.zerobase.fastlms.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryInput {
    private long id;
    private String categoryName;
    private int sortValue;
    private boolean usingYn;
}
