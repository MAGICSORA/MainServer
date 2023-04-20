package com.example.servertest.main.crop.model.request;

import lombok.*;

import java.io.File;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisDto {
    private long userId;
    private float userLatitude;
    private float userLongitude;
    private LocalDateTime regDate;
    private int cropType;
}
