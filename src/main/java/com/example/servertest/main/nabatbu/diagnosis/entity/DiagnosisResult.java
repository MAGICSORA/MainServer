package com.example.servertest.main.nabatbu.diagnosis.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
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
    private float accuracy;

    @Column
    private float boxX1;

    @Column
    private float boxX2;

    @Column
    private float boxY1;

    @Column
    private float boxY2;
}
