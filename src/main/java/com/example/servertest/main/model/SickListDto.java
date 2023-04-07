package com.example.servertest.main.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SickListDto {

    private String sickKey;
    private String cropName;
    private String sickNameKor;
    private String sickNameEng;
}
