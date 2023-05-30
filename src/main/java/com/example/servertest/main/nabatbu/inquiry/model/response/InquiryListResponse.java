package com.example.servertest.main.nabatbu.inquiry.model.response;

import com.example.servertest.main.nabatbu.inquiry.entity.Inquiry;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class InquiryListResponse {

    private long inquiryId;
    private String title;
    private LocalDateTime regDate;
    private int cnt;

    public static InquiryListResponse from(Inquiry item) {
        return InquiryListResponse.builder()
                .inquiryId(item.getId())
                .title(item.getTitle())
                .regDate(item.getRegDate())
                .build();
    }
}
