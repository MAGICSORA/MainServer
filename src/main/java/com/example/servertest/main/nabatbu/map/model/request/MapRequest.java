package com.example.servertest.main.nabatbu.map.model.request;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MapRequest {

    double latitude;
    double longitude;
    List<MapSheepCrop> mapSheepCropList;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date date;
}