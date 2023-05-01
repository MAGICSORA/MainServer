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
public class CropOccurInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private int warningListSize;

    @Column
    private String warningList;

    @Column
    private int watchListSize;

    @Column
    private String watchList;

    @Column
    private int forecastListSize;

    @Column
    private String forecastList;

    @Column
    private LocalDateTime updateDt;
}
