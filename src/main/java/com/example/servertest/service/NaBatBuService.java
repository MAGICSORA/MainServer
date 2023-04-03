package com.example.servertest.service;

import com.example.servertest.model.SickService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NaBatBuService {

    @Value("${ncpms.baseUrl}")
    private String BASE_URL;
    @Value("${ncpms.serviceKey}")
    private String serviceKey;

    private String makeSickListUrl(String cropName, String sickNameKor) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append("?apiKey=");
        sb.append(serviceKey);
        sb.append("&serviceCode=SVC01&serviceType=AA001&cropName=");
        sb.append(cropName);
        sb.append("&sickNameKor=");
        sb.append(sickNameKor);
        sb.append("&displayCount=30&startPoint=0");

        return sb.toString();
    }

    public SickService getSickList(String cropName, String sickNameKor)
        throws IOException {
        URL url = new URL(makeSickListUrl(cropName, sickNameKor));

//        System.out.println(url);
        log.info(url.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(
                new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        String xml = sb.toString();

//        System.out.println(xml);

//        Map<String, Service> result = new HashMap<>();
        SickService apiResponse = new SickService();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(
                SickService.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            apiResponse = (SickService) unmarshaller.unmarshal(
                new StringReader(xml));
//            result.put("service", apiResponse);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return apiResponse;

    }
}
