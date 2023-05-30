package com.example.servertest.main.nabatbu.category.model;

import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisRecord;
import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisResult;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DiagnosisOfCategoryResponse {

    private DiagnosisResult diagnosisResult;
}
