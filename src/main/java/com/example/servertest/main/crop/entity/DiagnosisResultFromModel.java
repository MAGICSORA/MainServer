package com.example.servertest.main.crop.entity;

import com.example.servertest.main.crop.model.DiagnosisResult;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DiagnosisResultFromModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private int responseCode;

    @Column
    private String diagnosisResults;
}
