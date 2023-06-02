package com.example.servertest.main.nabatbu.admin.model;

import com.example.servertest.main.nabatbu.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Slice;

@Getter
@Setter
@Builder
public class MemberListResponse {

    private Slice<Member> memberList;
    private int cnt;
}
