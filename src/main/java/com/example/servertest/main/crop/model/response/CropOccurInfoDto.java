package com.example.servertest.main.crop.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CropOccurInfoDto {

    int warningListSize;
    List warningList;

    int watchListSize;
    List watchList;

    int forecastListSize;
    List forecastList;
}
