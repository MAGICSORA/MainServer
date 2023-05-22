package com.example.servertest.main.nabatbu.cropInfo.psis.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PsisManager {

    @Value("${psis.baseUrl}")
    private String BASE_URL;
    @Value("${psis.apiKey}")
    private String apiKey;

    public String makePsisListRequestUrl(String cropType, String diseaseCode, String displayCount, String startPoint) {
        return BASE_URL + "?" +
                "apiKey=" + apiKey +
                "&serviceCode=SVC01&serviceType=AA001&cropName=" + cropType +
                "&diseaseWeedName=" + diseaseCode +
                "&displayCount=" + displayCount +
                "&startPoint=" + startPoint;
    }

    public String makePsisInfoRequestUrl(String pestiCode, String diseaseCode, String displayCount, String startPoint) {
        return BASE_URL + "?" +
                "apiKey=" + apiKey +
                "&serviceCode=SVC02&pestiCode=" + pestiCode +
                "&diseaseUseSeq=" + diseaseCode +
                "&displayCount=" + displayCount +
                "&startPoint=" + startPoint;
    }


}
