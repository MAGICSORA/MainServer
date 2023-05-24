package com.example.servertest.main.nabatbu.cropInfo.model.response;

import com.example.servertest.main.nabatbu.cropInfo.ncpms.model.response.NcpmsSickDetailService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SickDetailResponse {

    private String developmentCondition;
    private String symptoms;
    private String preventionMethod;
    private String infectionRoute;

    public static SickDetailResponse from(NcpmsSickDetailService item) {
        return SickDetailResponse.builder()
                .developmentCondition(item.getDevelopmentCondition())
                .symptoms(item.getSymptoms())
                .preventionMethod(item.getPreventionMethod())
                .infectionRoute(item.getInfectionRoute())
                .build();
    }
}
