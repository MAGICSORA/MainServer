package com.example.servertest.main.nabatbu.category.model;

import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisRecord;
import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisResult;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class DiagnosisOfCategoryResponse {

    private DiagnosisRecord diagnosisRecord;
    private List<DiagnosisResult> diagnosisResultList;
}
