package com.example.servertest.main.nabatbu.diagnosis.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DiagnosisRequest {

    private int cropType;
    private String cropImageId;
}
