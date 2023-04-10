package com.example.servertest.main.member.domain.service;

import com.example.servertest.main.member.global.jwt.TokenProvider;
import com.example.servertest.main.member.domain.entity.Member;
import com.example.servertest.main.member.domain.exception.MemberError;
import com.example.servertest.main.member.domain.exception.MemberException;
import com.example.servertest.main.member.domain.model.*;
import com.example.servertest.main.member.domain.repository.MemberRepository;
import com.example.servertest.main.member.domain.type.MemberType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.example.servertest.main.member.global.jwt.JwtAuthenticationFilter.TOKEN_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public RegisterMember.Response register(RegisterMember.Request parameter) {

        Optional<Member> optionalMember = memberRepository.findByEmail(parameter.getEmail());

        if (optionalMember.isPresent()) {
            throw new MemberException(MemberError.MEMBER_ALREADY_EMAIL);
        }

        String pw = BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt());

        return RegisterMember.Response.from(MemberDto.from(memberRepository.save(
                Member.builder()
                        .email(parameter.getEmail())
                        .password(pw)
                        .name(parameter.getName())
                        .phone(parameter.getPhone())
                        .regDt(LocalDateTime.now())
                        .type(MemberType.ROLE_READWRITE)
                        .build())));
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

    public MemberInfo getMemberInfo(Long memberId, String token) {

        Member member = validateMember(token);

        if (!Objects.equals(member.getId(), memberId)) {
            throw new MemberException((MemberError.INFO_MEMBER_UN_MATCH));
        }

        return MemberInfo.from(MemberDto.from(member));
    }

    @Transactional
    public ModifyMember.Response modifyMember(Long memberId, String token,
                                              ModifyMember.Request request) {

        Member member = validateMember(token);

        if (!Objects.equals(member.getId(), memberId)) {
            throw new MemberException((MemberError.MODIFY_MEMBER_UN_MATCH));
        }
        String rePw = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        member.setPassword(rePw);
        member.setPhone(request.getPhone());
        member.setEmail(request.getEmail());
        member.setUpdateDt(LocalDateTime.now());
        memberRepository.save(member);

        return ModifyMember.Response.from(MemberDto.from(member));
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

    private Member validateMember(String token) {

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
}