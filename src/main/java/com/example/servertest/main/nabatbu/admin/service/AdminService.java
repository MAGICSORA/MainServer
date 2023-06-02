package com.example.servertest.main.nabatbu.admin.service;

import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.nabatbu.member.entity.Member;
import com.example.servertest.main.nabatbu.member.exception.MemberError;
import com.example.servertest.main.nabatbu.member.repository.MemberRepository;
import com.example.servertest.main.nabatbu.member.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    public ServiceResult getMemberList(String token, String email, String name, int pageNum) {

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

        if (member.getAuthLevel() != 3) {
            MemberError error = MemberError.INVALID_AUTH;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }
/*
List<InquiryDetailResponse> inquiryDetailResponseListDesc = inquiryDetailResponseList
                .stream()
                .sorted(Comparator.comparing(InquiryDetailResponse::getRegDate).reversed())
                .collect(Collectors.toList());
 */
        PageRequest page = PageRequest.of(pageNum, 10);
        Slice<Member> memberList = memberRepository.findByEmailContainingAndNameContainingOrderByRegDtAsc(email, name, page);
        return ServiceResult.success(memberList);
    }

    public ServiceResult setMemberAuth(String token, Long userId, int setLevel) {
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

        if (member.getAuthLevel() != 3) {
            MemberError error = MemberError.INVALID_AUTH;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        Optional<Member> optionalMember = memberRepository.findById(userId);
        Member member1 = optionalMember.get();

        member1.setAuthLevel(setLevel);
        memberRepository.save(member1);
        return ServiceResult.success(member1);
    }
}