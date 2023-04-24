package com.example.servertest.main.crop.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CropError {

    DISEASE_NOT_FOUND("존재하지 않는 질병입니다.");

    private final String description;
}
