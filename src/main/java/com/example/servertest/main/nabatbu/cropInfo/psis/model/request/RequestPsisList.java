package com.example.servertest.main.nabatbu.cropInfo.psis.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestPsisList {

    private String cropName;
    private String diseaseWeedName;
    private String displayCount;
    private String startPoint;
}
