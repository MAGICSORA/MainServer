package com.example.servertest.main.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberError {

    MEMBER_ALREADY_EMAIL("이미 등록된 e-mail 입니다.")
    , MEMBER_NOT_FOUND("존재 하지 않는 ID 입니다.")
    , MEMBER_PASSWORD_NOT_SAME("등록된 비밀 번호가 다릅니다.")
    , MEMBER_ROLE_UN_ACCESSIBLE("정지 회원입니다.")
    , MODIFY_MEMBER_UN_MATCH("본인의 정보만 수정할 수 있습니다.")
    , INFO_MEMBER_UN_MATCH("본인의 정보만 조회할 수 있습니다.")
    , MEMBER_WRONG_APPROACH("잘못된 접근 입니다.")
    , MEMBER_ALREADY_NAME("이미 등록된 이름 입니다.")
    , INVALID_TOKEN("잘못 된 토큰 정보입니다.")
    , EXPIRED_TOKEN("만료된 토큰입니다.")
    , TOKEN_EMPTY("토큰이 없습니다.")
    , MEMBER_EMAIL_INVALID("올바른 형식의 이메일 주소여야 합니다.")
    , MEMBER_PASSWORD_INVALID("비밀번호는 6자리 이상이여야 합니다.");

    private final String description;
}
