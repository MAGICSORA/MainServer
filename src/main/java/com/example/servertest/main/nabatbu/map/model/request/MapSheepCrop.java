package com.example.servertest.main.nabatbu.map.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MapSheepCrop {

    int cropType;
    float accuracy;
    Boolean isOn;
}