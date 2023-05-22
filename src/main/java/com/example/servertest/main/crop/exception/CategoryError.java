package com.example.servertest.main.crop.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryError {

    CATEGORY_ALREADY_EXIST("이미 존재하는 카테고리입니다."), CATEGORY_NOT_FOUND("카테고리가 존재하지 않습니다."), INVALID_NAME_LENGTH("카테고리 이름은 1자 이상, 20자 이하여야 합니다."), INVALID_MEMO_LENGTH("메모는 200자 이하여야 합니다.");

    private final String description;
}
