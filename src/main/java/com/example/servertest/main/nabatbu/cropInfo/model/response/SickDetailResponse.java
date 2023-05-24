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
        SickDetailResponse response =  SickDetailResponse.builder()
                .developmentCondition(item.getDevelopmentCondition())
                .symptoms(item.getSymptoms())
                .preventionMethod(item.getPreventionMethod())
                .infectionRoute(item.getInfectionRoute())
                .build();

        if (response.developmentCondition == "") {
            response.developmentCondition = "해당 정보가 존재하지 않습니다.";
        }
        if (response.symptoms == "") {
            response.symptoms = "해당 정보가 존재하지 않습니다.";
        }
        if (response.preventionMethod == "") {
            response.preventionMethod = "해당 정보가 존재하지 않습니다.";
        }
        if (response.infectionRoute == "") {
            response.infectionRoute = "해당 정보가 존재하지 않습니다.";
        }

        return response;
    }
}
