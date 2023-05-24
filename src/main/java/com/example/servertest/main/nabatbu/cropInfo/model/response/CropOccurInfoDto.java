package com.example.servertest.main.nabatbu.cropInfo.model.response;

import lombok.*;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CropOccurInfoDto {

    int warningListSize;
    List<CropOccurDto> warningList;

    int watchListSize;
    List<CropOccurDto> watchList;

    int forecastListSize;
    List<CropOccurDto> forecastList;
}
