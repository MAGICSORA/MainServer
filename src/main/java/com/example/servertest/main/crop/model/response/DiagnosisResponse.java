package com.example.servertest.main.crop.model.response;

import com.example.servertest.main.crop.model.response.DiagnosisItem;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisResponse {

    private int responseCode;
    private int cropType;
    private LocalDateTime regDate;
    private List<DiagnosisItem> diagnosisItems;
    private String imagePath;
}