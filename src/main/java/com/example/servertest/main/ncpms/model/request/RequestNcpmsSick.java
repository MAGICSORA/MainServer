package com.example.servertest.main.ncpms.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestNcpmsSick {

    private String cropName;
    private String sickNameKor;
    private String displayCount;
    private String startPoint;
}
