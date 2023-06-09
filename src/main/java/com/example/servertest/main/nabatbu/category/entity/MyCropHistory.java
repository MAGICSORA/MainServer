package com.example.servertest.main.nabatbu.category.entity;

import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisRecord;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyCropHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn
    private DiagnosisRecord diagnosisRecord;

    @Column
    private LocalDateTime regDt;

    @Column
    private LocalDateTime updateDt;

    @Column
    private String contents;
}
