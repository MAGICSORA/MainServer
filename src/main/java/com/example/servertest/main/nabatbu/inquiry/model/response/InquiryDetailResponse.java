package com.example.servertest.main.nabatbu.inquiry.model.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class InquiryDetailResponse {

    private long id;

    private long userId;

    private long diagnosisRecordId;

    private String title;

    private String contents;

    private LocalDateTime regDate;

    private long replyId;
}
