package com.example.servertest.main.nabatbu.diagnosis.entity;

import com.example.servertest.main.nabatbu.diagnosis.model.response.DiagnosisResultOutput;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class DiagnosisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private int responseCode;

    @ManyToOne
    @JoinColumn
    private DiagnosisRecord diagnosisRecord;

    @Column
    private int diseaseCode;

    @Column
    private String sickKey;

    @Column
    private double accuracy;

    @Column
    private double boxX1;

    @Column
    private double boxX2;

    @Column
    private double boxY1;

    @Column
    private double boxY2;
}
