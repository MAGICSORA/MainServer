package com.example.servertest.main.crop.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MapSheepCrop {

    int cropType;
    float accuracy;
    boolean isOn;
}