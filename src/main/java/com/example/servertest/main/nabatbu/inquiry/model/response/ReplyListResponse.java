package com.example.servertest.main.nabatbu.inquiry.model.response;

import com.example.servertest.main.nabatbu.inquiry.entity.Inquiry;
import com.example.servertest.main.nabatbu.inquiry.entity.Reply;
import com.example.servertest.main.nabatbu.inquiry.repository.InquiryRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReplyListResponse {

    private final InquiryRepository inquiryRepository;

    private long replyId;
    private String inquiryTitle;
    private LocalDateTime regDate;
}
