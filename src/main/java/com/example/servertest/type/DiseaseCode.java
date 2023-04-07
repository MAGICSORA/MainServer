package com.example.servertest.type;

import lombok.Getter;

@Getter
public enum DiseaseCode {

    PEPPER_NORMAL("고추 정상"),
    PEPPER_MILD_MOTTLE("고추마일드모틀바이러스"),
    PEPPER_BACTERIAL_SPOT("고추점무늬병"),
    STRAWBERRY_NORMAL("딸기정상"),
    STRAWBERRY_GRAY_MOLD("딸기잿빛곰팡이병"),
    STRAWBERRY_POWDERY_MILDEW("딸기흰가루병"),
    LETTUCE_NORMAL("상추정상"),
    LETTUCE_SCLEROTINIA_ROT("상추균핵병"),
    LETTUCE_DOWNY_MILDEW("상추노균병"),
    TOMATO_NORMAL("토마토정상"),
    TOMATO_LEAF_MOLD("토마토잎곰팡이병"),
    TOMATO_YELLOW_LEAF_CURL("토마토황화잎말이바이러스");

    private String value;

    DiseaseCode(String value) {
        this.value = value;
    }
}
