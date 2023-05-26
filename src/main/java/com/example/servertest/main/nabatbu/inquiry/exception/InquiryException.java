package com.example.servertest.main.nabatbu.inquiry.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class InquiryException extends RuntimeException{

    private InquiryError inquiryError;
//    private String error;

    public InquiryException(InquiryError inquiryError) {
        super(inquiryError.getDescription());
        this.inquiryError = inquiryError;
    }
}
