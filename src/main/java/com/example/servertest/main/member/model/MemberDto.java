package com.example.servertest.main.member.model;

import java.time.LocalDateTime;

import com.example.servertest.main.member.entity.Member;
import com.example.servertest.main.member.type.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MemberDto {

    private String name;
    private String email;
    private String password;
    private MemberType type;
    private LocalDateTime regDt;
    private LocalDateTime updateDt;

    public static MemberDto from(Member member) {
        return MemberDto.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .name(member.getName())
                .type(member.getType())
                .regDt(member.getRegDt())
                .updateDt(member.getUpdateDt())
                .build();
    }

}
