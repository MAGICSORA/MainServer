package com.example.servertest.main.nabatbu.diagnosis.model.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisDto {
//    private long userId;
    // -> token
    private double userLatitude;
    private double userLongitude;
    private LocalDateTime regDate;
    private int cropType;
}
