package com.example.servertest.main.psis.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPsisList {

    private String cropType;
    private String diseaseCode;
    private String displayCount;
    private String startPoint;
}
