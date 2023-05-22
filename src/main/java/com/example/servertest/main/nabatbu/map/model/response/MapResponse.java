package com.example.servertest.main.nabatbu.map.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MapResponse {

    private String imagePath;
    private LocalDateTime localDateTime;
    private int cropType;
    private double longitude;
    private double latitude;
    private float accuracy;
    private int diseaseCode;
}
