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
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long inquiryId;

    @Column
    private long userId;

    @Column
    private String contents;

    @Column
    private LocalDateTime regDate;
}
