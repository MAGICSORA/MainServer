package com.example.servertest.main.crop.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiagnosisResult {

    private int diseaseCode;
    private float accuracy;
    private float[] bBox;
}
