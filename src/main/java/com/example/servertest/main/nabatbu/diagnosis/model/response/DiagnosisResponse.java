package com.example.servertest.main.nabatbu.diagnosis.model.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisResponse {

    private long diagnosisRecordId;
    private int responseCode;
    private int cropType;
    private long categoryId;
    private LocalDateTime regDate;
    private List<DiagnosisResultOutput> diagnosisResults;
    private String imagePath;
}
