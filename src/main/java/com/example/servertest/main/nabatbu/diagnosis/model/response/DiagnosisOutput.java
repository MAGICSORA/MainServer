package com.example.servertest.main.nabatbu.diagnosis.model.response;

import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisResult;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisOutput {

    private int diseaseCode;
    private double accuracy;
    private List<Double> bbox;
//    private double boxX2;
//    private double boxY1;
//    private double boxY2;

    public static DiagnosisOutput to(DiagnosisResult diagnosisResult) {
        List<Double> list = List.of(diagnosisResult.getBoxX1(), diagnosisResult.getBoxY1(), diagnosisResult.getBoxX2(), diagnosisResult.getBoxY2());
        return DiagnosisOutput.builder()
                .diseaseCode(diagnosisResult.getDiseaseCode())
                .accuracy(diagnosisResult.getAccuracy())
                .bbox(list)
                .build();
    }
}
