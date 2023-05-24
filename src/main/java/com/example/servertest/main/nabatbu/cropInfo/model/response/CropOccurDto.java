package com.example.servertest.main.nabatbu.cropInfo.model.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CropOccurDto {

    private String cropName;
    private String sickNameKor;
    private String sickKey;
}
