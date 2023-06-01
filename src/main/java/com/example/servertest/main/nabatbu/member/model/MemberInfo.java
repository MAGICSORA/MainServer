package com.example.servertest.main.nabatbu.member.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInfo {

    private String name;
    private String email;
    private int authLevel;
//    private MemberType type;

    public static MemberInfo from(MemberDto memberDto) {

        return MemberInfo.builder()
                .name(memberDto.getName())
                .email(memberDto.getEmail())
//                .authLevel(memberDto.get)
//                .type(memberDto.getType())
                .build();
    }

}
