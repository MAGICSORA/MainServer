package com.example.servertest.main.nabatbu.diagnosis.model.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDiagnosisRecord {

    private int userId;
    private double userLatitude;
    private double userLongitude;
    private int cropType;
    private long categoryId;
    private List<DiagnosisResultOutput> diagnosisResults;
    private String imagePath;
    private LocalDateTime regDate;
}
