package com.example.servertest.main.nabatbu.inquiry.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long userId;

    @Column
    private long diagnosisRecordId;

    @Column
    private String title;

    @Column
    private String contents;

    @Column
    private LocalDateTime regDate;
}
