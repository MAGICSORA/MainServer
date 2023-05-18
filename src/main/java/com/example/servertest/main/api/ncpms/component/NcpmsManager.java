package com.example.servertest.main.api.ncpms.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NcpmsManager {

    @Value("${ncpms.baseUrl}")
    private String BASE_URL;
    @Value("${ncpms.serviceKey}")
    private String apiKey;

    public String makeNcpmsSickSearchRequestUrl(String cropName, String sickNameKor, String displayCount, String startPoint) {
        return BASE_URL + "?" +
                "apiKey=" + apiKey +
                "&serviceCode=SVC01&serviceType=AA001&cropName=" + cropName +
                "&sickNameKor=" + sickNameKor +
                "&displayCount=" + displayCount +
                "&startPoint=" + startPoint;
    }

    public String makeNcpmsSickDetailSearchRequestUrl(String sickKey) {
        return BASE_URL + "?" +
                "apiKey=" + apiKey +
                "&serviceCode=SVC05&serviceType=AA001&sickKey=" + sickKey;
    }
}
