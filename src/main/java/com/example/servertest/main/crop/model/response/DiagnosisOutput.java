package com.example.servertest.main.crop.model.response;

import com.example.servertest.main.crop.entity.DiagnosisResult;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisOutput {

    private int diseaseCode;
    private float accuracy;
    private float boxX1;
    private float boxX2;
    private float boxY1;
    private float boxY2;

    public static DiagnosisOutput to(DiagnosisResult diagnosisResult) {
        return DiagnosisOutput.builder()
                .diseaseCode(diagnosisResult.getDiseaseCode())
                .accuracy(diagnosisResult.getAccuracy())
                .boxX1(diagnosisResult.getBoxX1())
                .boxX2(diagnosisResult.getBoxX2())
                .boxY1(diagnosisResult.getBoxY1())
                .boxY2(diagnosisResult.getBoxY2()).build();
    }
}
