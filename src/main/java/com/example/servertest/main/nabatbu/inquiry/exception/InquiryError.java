package com.example.servertest.main.nabatbu.inquiry.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InquiryError {

    INQUIRY_NOT_FOUND("질문 내역이 존재하지 않습니다.");

    private final String description;
}
