package com.example.servertest.main.test.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InputDiseaseDetail {

    private long id;
    private String controlMethod;
    private String environment;
    private String symptom;
}
