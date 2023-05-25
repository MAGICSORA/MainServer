package com.example.servertest.main.nabatbu.inquiry.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RequestInquiry {

    private long diagnosisRecordId;
    private String title;
    private String contents;
//    private LocalDateTime localDateTime;
}
