package com.example.servertest.main.crop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SickList {

    @Id
    private String sickKey;

    @Column
    private String cropName;

    @Column
    private String sickNameKor;

    @Column
    private String sickNameEng;
}
