package com.example.servertest.main.global.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResult {

    private boolean result;
    private String message;
    private Object object;

    public static ServiceResult fail(String message) {
        return ServiceResult.builder()
                .result(false)
                .message(message)
                .build();
    }

    public static ServiceResult success(Object object) {
        return ServiceResult.builder()
                .result(true)
                .object(object)
                .build();
    }

    public static ServiceResult success() {
        return ServiceResult.builder()
                .result(true)
                .build();
    }

    public boolean isFail() {
        return !result;
    }
}