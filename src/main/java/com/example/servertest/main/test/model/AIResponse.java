package com.example.servertest.main.test.model;

import com.example.servertest.main.crop.model.response.DiagnosisOutput;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIResponse {

    private int responseCode;
    private List<DiagnosisOutput> diagnosisResult;
}
