package com.example.servertest.main.crop.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NcpmsError {

    NO_DATA_EXIST("관련 정보가 없습니다.");

    private final String description;
}
