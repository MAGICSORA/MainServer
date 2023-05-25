package com.example.servertest.main.nabatbu.diagnosis.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DiagnosisResultOutput {

    private int diseaseCode;
    private String sickKey;
    private double accuracy;
    private double boxX1;
    private double boxY1;
    private double boxX2;
    private double boxY2;

    public static DiagnosisResultOutput from(DiagnosisOutput item) {
        String key;
        if (item.getDiseaseCode() == 1) {
            key = "D00000189";
        } else if (item.getDiseaseCode() == 2) {
            key = "D00000194";
        } else if (item.getDiseaseCode() == 4) {
            key = "D00000440";
        } else if (item.getDiseaseCode() == 5) {
            key = "D00000459";
        } else if (item.getDiseaseCode() == 7) {
            key = "D00000964";
        } else if (item.getDiseaseCode() == 8) {
            key = "D00000957";
        } else if (item.getDiseaseCode() == 10) {
            key = "D00001533";
        } else if (item.getDiseaseCode() == 11) {
            key = null;
        } else {
            key = null;
        }

        return DiagnosisResultOutput.builder()
                .diseaseCode(item.getDiseaseCode())
                .accuracy(item.getAccuracy())
                .boxX1(item.getBbox().get(0))
                .boxY1(item.getBbox().get(1))
                .boxX2(item.getBbox().get(2))
                .boxY2(item.getBbox().get(3))
                .sickKey(key)
                .build();
    }
}
