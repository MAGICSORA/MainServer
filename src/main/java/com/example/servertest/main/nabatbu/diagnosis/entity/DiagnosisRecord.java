package com.example.servertest.main.nabatbu.diagnosis.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiagnosisRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long userId;

    @Column
    private double userLatitude;

    @Column
    private double userLongitude;

    @Column
    private LocalDateTime regDate;

    @Column
    private int cropType;

    @Column
    private String imagePath;

    @Column
    private long categoryId;
}

