package com.example.servertest.main.crop.model.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiagnosisItemtmp {

    private int diseaseCode;
    private float accuracy;
    private float boxX1;
    private float boxX2;
    private float boxY1;
    private float boxY2;

}
