package com.example.servertest.main.crop.model.response;

import com.example.servertest.main.crop.entity.DiagnosisResult;
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
    private float userLatitude;
    private float userLongitude;
    private int cropType;
    private List<DiagnosisOutput> diagnosisResults;
    private String imagePath;
    private LocalDateTime regDate;

}
