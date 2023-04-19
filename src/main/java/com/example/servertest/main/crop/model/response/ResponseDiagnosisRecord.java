package com.example.servertest.main.crop.model.response;

import com.example.servertest.main.crop.model.DiagnosisItem;
import lombok.*;

import java.time.LocalDateTime;

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
    private DiagnosisItem[] diagnosisItems;
    private String imagePath;
    private LocalDateTime regDate;

}
