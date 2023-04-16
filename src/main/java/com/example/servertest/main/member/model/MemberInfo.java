package com.example.servertest.main.member.model;

import com.example.servertest.main.member.type.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInfo {

    private String name;
    private String email;
    private MemberType type;

    public static MemberInfo from(MemberDto memberDto) {

        return MemberInfo.builder()
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .type(memberDto.getType())
                .build();
    }

}
