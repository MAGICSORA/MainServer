package com.example.servertest.main.nabatbu.inquiry.service;

import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.nabatbu.inquiry.entity.Inquiry;
import com.example.servertest.main.nabatbu.inquiry.entity.Reply;
import com.example.servertest.main.nabatbu.inquiry.model.request.RequestInquiry;
import com.example.servertest.main.nabatbu.inquiry.model.response.InquiryDetailResponse;
import com.example.servertest.main.nabatbu.inquiry.model.response.InquiryListResponse;
import com.example.servertest.main.nabatbu.inquiry.repository.InquiryRepository;
import com.example.servertest.main.nabatbu.inquiry.repository.ReplyRepository;
import com.example.servertest.main.nabatbu.member.entity.Member;
import com.example.servertest.main.nabatbu.member.exception.MemberError;
import com.example.servertest.main.nabatbu.member.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final MemberService memberService;

    private final InquiryRepository inquiryRepository;
    private final ReplyRepository replyRepository;

    public ServiceResult register(String token, RequestInquiry requestInquiry) {

        Member member;
        try {
            member = memberService.validateMember(token);
        } catch (ExpiredJwtException e) {
            MemberError error = MemberError.EXPIRED_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        Inquiry inquiry = Inquiry.builder()
                .userId(member.getId())
                .diagnosisRecordId(requestInquiry.getDiagnosisRecordId())
                .title(requestInquiry.getTitle())
                .contents(requestInquiry.getContents())
                .regDate(LocalDateTime.now())
                .build();

        inquiryRepository.save(inquiry);

        return ServiceResult.success(inquiry);
    }

    public ServiceResult returnInquiryList(String token) {

        Member member;
        try {
            member = memberService.validateMember(token);
        } catch (ExpiredJwtException e) {
            MemberError error = MemberError.EXPIRED_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        List<Inquiry> inquiryList = inquiryRepository.findAllByUserId(member.getId());
        List<InquiryListResponse> inquiryListResponseList = new ArrayList<>();
        for (Inquiry inquiry : inquiryList) {
            InquiryListResponse inquiryListResponse = InquiryListResponse.from(inquiry);
            inquiryListResponse.setCnt(replyRepository.countByInquiryId(inquiryListResponse.getInquiryId()));
            inquiryListResponseList.add(InquiryListResponse.from(inquiry));
        }

        return ServiceResult.success(inquiryListResponseList);
    }

    public ServiceResult getInquiryDetail(String token, Long inquiryId) {

        Member member;
        try {
            member = memberService.validateMember(token);
        } catch (ExpiredJwtException e) {
            MemberError error = MemberError.EXPIRED_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(inquiryId);
        Inquiry inquiry = optionalInquiry.get();

        Optional<Reply> reply = Optional.ofNullable(replyRepository.findByInquiryId(inquiry.getId()));
        Long replyId = -1L;
        if (reply.isPresent()) {
            replyId = reply.get().getId();
        }
        InquiryDetailResponse inquiryDetailResponse = InquiryDetailResponse.builder()
                .id(inquiry.getId())
                .userId(inquiry.getUserId())
                .diagnosisRecordId(inquiry.getDiagnosisRecordId())
                .title(inquiry.getTitle())
                .contents(inquiry.getContents())
                .regDate(inquiry.getRegDate())
                .replyId(replyId)
                .build();

        return ServiceResult.success(inquiryDetailResponse);
    }

    public ServiceResult deleteInquiry(String token, Long inquiryId) {

        Member member;
        try {
            member = memberService.validateMember(token);
        } catch (ExpiredJwtException e) {
            MemberError error = MemberError.EXPIRED_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(inquiryId);
        Inquiry inquiry = optionalInquiry.get();
        inquiryRepository.delete(inquiry);

        return ServiceResult.success();
    }
}
