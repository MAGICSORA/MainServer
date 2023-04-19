package com.example.servertest.main.crop.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiagnosisItem {

    private int diseaseCode;
    private float accuracy;
    private float boxX1;
    private float boxX2;
    private float boxY1;
    private float boxY2;

}
