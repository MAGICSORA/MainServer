package com.example.servertest.main.crop.model.request;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MapRequest {

    double latitude;
    double longitude;
    List<MapSheepCrop> mapSheepCropList;
    Date date;
}