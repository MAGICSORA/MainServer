package com.example.servertest.main.crop.entity;

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
    private long diagnosisResultId;

    @Column
    private float userLatitude;

    @Column
    private float userLongitude;

    @Column
    private LocalDateTime regDate;

    @Column
    private int cropType;

    @Column
    private String imagePath;
}

