package com.example.servertest.main.global.type;

import lombok.Getter;

@Getter
public enum CropType {

    PEPPER("고추"),
    STRAWBERRY("딸기"),
    LETTUCE("상추"),
    TOMATO("토마토");

    private String value;

    CropType(String value) {
        this.value = value;
    }
}
