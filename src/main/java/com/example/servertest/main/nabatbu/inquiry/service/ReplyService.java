package com.example.servertest.main.nabatbu.inquiry.service;

import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.nabatbu.inquiry.entity.Inquiry;
import com.example.servertest.main.nabatbu.inquiry.entity.Reply;
import com.example.servertest.main.nabatbu.inquiry.exception.InquiryError;
import com.example.servertest.main.nabatbu.inquiry.model.request.RequestReply;
import com.example.servertest.main.nabatbu.inquiry.model.response.InquiryDetailResponse;
import com.example.servertest.main.nabatbu.inquiry.model.response.ReplyListResponse;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final MemberService memberService;

    private final ReplyRepository replyRepository;
    private final InquiryRepository inquiryRepository;

    public ServiceResult register(String token, RequestReply requestReply) {

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

        if (member.getAuthLevel() != 2) {
            MemberError error = MemberError.INVALID_AUTH;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(requestReply.getInquiryId());
        if (optionalInquiry.isEmpty()) {
            InquiryError error = InquiryError.INQUIRY_NOT_FOUND;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        Reply reply = Reply.builder()
                .inquiryId(requestReply.getInquiryId())
                .userId(member.getId())
                .contents(requestReply.getContents())
                .regDate(LocalDateTime.now())
                .build();

        replyRepository.save(reply);

        return ServiceResult.success(reply);
    }

    public ServiceResult returnReplyList(String token) {

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

        if (member.getAuthLevel() != 2) {
            MemberError error = MemberError.INVALID_AUTH;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        List<Reply> replyList = replyRepository.findAllByUserId(member.getId());
        List<ReplyListResponse> replyListResponses = new ArrayList<>();
        for (Reply reply : replyList) {
            Optional<Inquiry> optionalInquiry = inquiryRepository.findById(reply.getInquiryId());
            Inquiry inquiry = optionalInquiry.get();

            ReplyListResponse replyListResponse = ReplyListResponse.builder()
                    .replyId(reply.getId())
                    .inquiryTitle(inquiry.getTitle())
                    .regDate(reply.getRegDate())
                    .build();
            replyListResponses.add(replyListResponse);
        }
        return ServiceResult.success(replyListResponses);
    }

    public ServiceResult getReplyDetail(String token, Long replyId) {

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

//        if (member.getAuthLevel() != 2) {
//            MemberError error = MemberError.INVALID_AUTH;
//            return ServiceResult.fail(String.valueOf(error), error.getDescription());
//        }

        Optional<Reply> optionalReply = replyRepository.findById(replyId);
        Reply reply = optionalReply.get();

        return ServiceResult.success(reply);
    }

    public ServiceResult deleteReply(String token, Long replyId) {

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

        if (member.getAuthLevel() != 2) {
            MemberError error = MemberError.INVALID_AUTH;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        Optional<Reply> optionalReply = replyRepository.findById(replyId);
        Reply reply = optionalReply.get();
        replyRepository.delete(reply);

        return ServiceResult.success();
    }

    public ServiceResult update(String token, Long replyId, String contents) {

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

        if (member.getAuthLevel() != 2) {
            MemberError error = MemberError.INVALID_AUTH;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        Optional<Reply> optionalReply = replyRepository.findById(replyId);
        Reply reply = optionalReply.get();

        reply.setContents(contents);
        reply.setRegDate(LocalDateTime.now());

        replyRepository.save(reply);

        return ServiceResult.success(reply);
    }

    public ServiceResult showList(String token) {

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

        if (member.getAuthLevel() != 2) {
            MemberError error = MemberError.INVALID_AUTH;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        List<Inquiry> inquiryList = inquiryRepository.findAll();

        List<InquiryDetailResponse> inquiryDetailResponseList = new ArrayList<>();
        for (Inquiry inquiry : inquiryList) {
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
            inquiryDetailResponseList.add(inquiryDetailResponse);
        }

        List<InquiryDetailResponse> inquiryDetailResponseListDesc = inquiryDetailResponseList
                .stream()
                .sorted(Comparator.comparing(InquiryDetailResponse::getRegDate).reversed())
                .collect(Collectors.toList());
        return ServiceResult.success(inquiryDetailResponseListDesc);
    }
}
