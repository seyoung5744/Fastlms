package com.zerobase.fastlms.course.model;

import com.zerobase.fastlms.admin.model.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ServiceResult {
    private boolean result;
    private String message;

    public ServiceResult(){}

    public ServiceResult(boolean result, String message){
        this.result = result;
        this.message = message;
    }

    public ServiceResult(boolean result) {
        this.result = result;
    }
}
