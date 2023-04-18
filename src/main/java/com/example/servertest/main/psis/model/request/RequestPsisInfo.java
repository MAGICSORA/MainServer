package com.example.servertest.main.psis.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestPsisInfo {

    private String pestiCode;
    private String diseaseUseSeq;
    private String displayCount;
    private String startPoint;
}
