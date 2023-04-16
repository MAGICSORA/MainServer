package com.example.servertest.main.member.model;

import java.time.LocalDateTime;

import com.example.servertest.main.member.type.MemberType;
import lombok.*;

public class RegisterMember {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Request {

//        @NotBlank(message = "사용자 ID(e-mail)는 필수 항목입니다.")
//        @Pattern(regexp = "^[a-z0-9A-Z._-]*@[a-z0-9A-Z]*.[a-zA-Z.]*$", message = "이메일 형식을 올바르게 입력해주세요.")
        private String email;

//        @NotBlank(message = "사용자 비밀번호는 필수 항목입니다.")
//        @Pattern(regexp = "[a-zA-Z1-9]{6,12}", message = "비밀번호는 영어와 숫자로 포함해서 6 ~ 12자리 이내로 입력해주세요.")
        private String password;

//        @NotBlank(message = "사용자 이름은 필수 항목입니다.")
//        @Pattern(regexp = "[가-힣]*$", message = "한국어로 입력해주세요.")
//        @Size(min = 2, max = 8, message = "이름을 2~8자 사이 이내로 입력해주세요.")
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        private String email;
        private String name;
        private LocalDateTime regDt;
        private MemberType type;

        public static RegisterMember.Response from(MemberDto memberDto) {

            return RegisterMember.Response.builder()
                    .email(memberDto.getEmail())
                    .name(memberDto.getName())
                    .regDt(memberDto.getRegDt())
                    .type(memberDto.getType())
                    .build();
        }

    }

}