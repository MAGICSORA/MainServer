package com.example.servertest.main.api.psis.service;

import com.example.servertest.main.api.psis.model.response.PsisInfoService;
import com.example.servertest.main.api.psis.model.response.PsisListService;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.member.entity.Member;
import com.example.servertest.main.member.exception.MemberError;
import com.example.servertest.main.member.service.MemberService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PsisService {
    private final MemberService memberService;

    public ServiceResult returnResult(String urlBuilder, boolean type, String token) throws IOException {
        Member member;
        try {
            member = memberService.validateMember(token);
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }
        URL url = new URL(urlBuilder);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        // String 형식의 xml
        String xml = sb.toString();

        if (type) {
            // String 형식의 xml을 Java Object인 Response로 변환
            Map<String, PsisListService> result = new HashMap<>();
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(PsisListService.class); // JAXB Context 생성
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();  // Unmarshaller Object 생성
                PsisListService apiResponse = (PsisListService) unmarshaller.unmarshal(new StringReader(xml)); // unmarshall 메서드 호출
                result.put("response", apiResponse);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
            return ServiceResult.success(result);
        } else {
            // String 형식의 xml을 Java Object인 Response로 변환
            Map<String, PsisInfoService> result = new HashMap<>();
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(PsisInfoService.class); // JAXB Context 생성
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();  // Unmarshaller Object 생성
                PsisInfoService apiResponse = (PsisInfoService) unmarshaller.unmarshal(new StringReader(xml)); // unmarshall 메서드 호출
                result.put("response", apiResponse);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
            return ServiceResult.success(result);
        }
    }
}
