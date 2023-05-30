package com.example.servertest.main.nabatbu.inquiry.model.response;

import com.example.servertest.main.nabatbu.inquiry.entity.Inquiry;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InquiryList {
    private Inquiry inquiry;
    private Long replyId;
}
