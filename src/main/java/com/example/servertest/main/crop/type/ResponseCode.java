package com.example.servertest.main.crop.type;

import lombok.Getter;

@Getter
public enum ResponseCode {

    DIAGNOSIS_SUCCEED("진단 결과 있음"),
    DIAGNOSIS_FAILED("진단 결과 없음"),
    TIME_OUT("TimeOut");

    private String value;

    ResponseCode(String value) {
        this.value = value;
    }
}
