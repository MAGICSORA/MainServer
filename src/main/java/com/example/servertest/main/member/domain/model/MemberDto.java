package com.example.servertest.main.member.domain.model;

import java.time.LocalDateTime;

import com.example.servertest.main.member.domain.entity.Member;
import com.example.servertest.main.member.domain.type.MemberType;
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
    private String phone;
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
                .phone(member.getPhone())
                .type(member.getType())
                .regDt(member.getRegDt())
                .updateDt(member.getUpdateDt())
                .build();
    }

}
