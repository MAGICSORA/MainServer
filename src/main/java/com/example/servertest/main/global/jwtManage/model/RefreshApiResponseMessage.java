package com.example.servertest.main.global.jwtManage.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshApiResponseMessage {
    private Map<String, String> map;
}
