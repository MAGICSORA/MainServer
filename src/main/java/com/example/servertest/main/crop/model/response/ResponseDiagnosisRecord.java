package com.example.servertest.main.crop.model.response;

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
    private List<DiagnosisOutput> diagnosisResults;
    private String imagePath;
    private LocalDateTime regDate;
}
