package com.example.servertest.main.test.model;

import com.example.servertest.main.nabatbu.diagnosis.model.response.DiagnosisOutput;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AIResponse {

    private int responseCode;
    private List<DiagnosisOutput> diagnoseResults;
    private String cropImageId;
}
