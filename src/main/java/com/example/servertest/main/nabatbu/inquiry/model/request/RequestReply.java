package com.example.servertest.main.nabatbu.inquiry.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestReply {

    private long inquiryId;
    private String contents;
}
