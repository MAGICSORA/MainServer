package com.example.servertest.main.nabatbu.member.service;

import com.example.servertest.main.global.jwtManage.jwt.TokenProvider;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.nabatbu.member.entity.Member;
import com.example.servertest.main.nabatbu.member.exception.MemberError;
import com.example.servertest.main.nabatbu.member.exception.MemberException;
import com.example.servertest.main.nabatbu.member.model.*;
import com.example.servertest.main.nabatbu.member.repository.MemberRepository;
import com.example.servertest.main.nabatbu.member.type.MemberType;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.example.servertest.main.global.jwtManage.jwt.JwtAuthenticationFilter.TOKEN_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    private final MemberRepository memberRepository;

    public ServiceResult register(RegisterMember.Request parameter) {

        if (!parameter.getEmail().contains("@")) {
            MemberError e = MemberError.MEMBER_EMAIL_INVALID;
            return ServiceResult.fail(String.valueOf(e), e.getDescription());
        }

        if (parameter.getPassword().length() < 6) {
            MemberError e = MemberError.MEMBER_PASSWORD_INVALID;
            return ServiceResult.fail(String.valueOf(e), e.getDescription());
        }

        Optional<Member> optionalMember = memberRepository.findByEmail(parameter.getEmail());

        if (optionalMember.isPresent()) {
            MemberError e = MemberError.MEMBER_ALREADY_EMAIL;
            return ServiceResult.fail(String.valueOf(e), e.getDescription());
        }

//        Optional<Member> optionalMember1 = memberRepository.findByName(parameter.getName());
//        if (optionalMember1.isPresent()) {
//            MemberError e = MemberError.MEMBER_ALREADY_NAME;
//            return ServiceResult.fail(String.valueOf(e), e.getDescription());
//        }

        String pw = BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt());

        return ServiceResult.success(
                RegisterMember.Response.from(MemberDto.from(memberRepository.save(
                        Member.builder()
                                .email(parameter.getEmail())
                                .password(pw)
                                .name(parameter.getName())
                                .regDt(LocalDateTime.now())
                                .type(MemberType.ROLE_READWRITE)
                                .authLevel(1)
                                .build()))));
    }

    public Member login(LoginMember parameter) {

        Member member = memberRepository.findByEmail(parameter.getEmail())
                .orElseThrow(() -> new MemberException(MemberError.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(parameter.getPassword(), member.getPassword())) {
            throw new MemberException(MemberError.MEMBER_PASSWORD_NOT_SAME);
        }

        if (MemberType.ROLE_UN_ACCESSIBLE.equals(member.getType())) {
            throw new MemberException(MemberError.MEMBER_ROLE_UN_ACCESSIBLE);
        }

        return member;
    }

    public ServiceResult getMemberInfo(String token) {

        Member member = new Member();
        try {
            member = validateMember(token);
        } catch (ExpiredJwtException e) {
            MemberError error = MemberError.EXPIRED_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
//            e.printStackTrace();
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

//        Member member = validateMember(token);
        MemberInfo memberInfo = MemberInfo.from(MemberDto.from(member));
        memberInfo.setAuthLevel(member.getAuthLevel());

        return ServiceResult.success(memberInfo);
    }

    public void withDrawMember(Long memberId, String token, WithDrawMember request) {
        Member member = validateMember(token);

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new MemberException(MemberError.MEMBER_PASSWORD_NOT_SAME);
        }

        if (!Objects.equals(member.getId(), memberId)) {
            throw new MemberException((MemberError.MEMBER_WRONG_APPROACH));
        }

        member.setType(MemberType.ROLE_UN_ACCESSIBLE);
        memberRepository.save(member);
    }

    public Member validateMember(String token) {

        String subToken = token.substring(TOKEN_PREFIX.length());

        String email = "";
        email = tokenProvider.getUserPk(subToken);

        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberError.MEMBER_NOT_FOUND));
    }

    public String getName(String token) {
        return memberRepository.findByEmail(tokenProvider.getUserPk(token))
                .orElseThrow(() -> new MemberException(MemberError.MEMBER_NOT_FOUND)).getName();
    }

    public ServiceResult checkToken(String token) {
        Member member = new Member();
        try {
            member = validateMember(token);
        } catch (ExpiredJwtException e) {
            MemberError error = MemberError.EXPIRED_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }
        return ServiceResult.success();
    }
}