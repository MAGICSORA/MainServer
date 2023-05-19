package com.example.servertest.main.test.service;

import com.example.servertest.main.crop.entity.Category;
import com.example.servertest.main.crop.entity.DiagnosisRecord;
import com.example.servertest.main.crop.entity.DiagnosisResult;
import com.example.servertest.main.crop.model.request.DiagnosisDto;
import com.example.servertest.main.crop.model.response.DiagnosisOutput;
import com.example.servertest.main.crop.model.response.DiagnosisResponse;
import com.example.servertest.main.crop.repository.CategoryRepository;
import com.example.servertest.main.crop.repository.DiagnosisRecordRepository;
import com.example.servertest.main.crop.repository.DiagnosisResultRepository;
import com.example.servertest.main.crop.service.FileService;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.member.entity.Member;
import com.example.servertest.main.member.exception.MemberException;
import com.example.servertest.main.member.service.MemberService;
import com.example.servertest.main.test.entity.TestEntity;
import com.example.servertest.main.test.model.AIResponse;
import com.example.servertest.main.test.repository.TestRepository;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final MemberService memberService;
    private final DiagnosisRecordRepository diagnosisRecordRepository;
    private final FileService fileService;
    private final CategoryRepository categoryRepository;
    private final DiagnosisResultRepository diagnosisResultRepository;

    public void test(String input) {
        testRepository.save(TestEntity.builder().content(input).build());
    }

    public List showAll() {
        List<TestEntity> testEntityList = testRepository.findAll();
        List list = new ArrayList();
        for (TestEntity test : testEntityList) {
            list.add(test.getContent());
        }
        return list;
    }

    public void deleteTest(String input) {
        TestEntity test = testRepository.findByContent(input);
        testRepository.delete(test);
    }

    public ServiceResult returnDiagnosisResult(DiagnosisDto diagnosisDto, MultipartFile file, String token) throws IOException {

        Member member;
        try {
            member = memberService.validateMember(token);
        } catch (MemberException e) {
            return ServiceResult.fail(String.valueOf(e.getMemberError()), e.getMessage());
        }

        StringBuilder imgCode = new StringBuilder();
        imgCode.append(member.getId());
        imgCode.append("-");

        Long cnt;
        if (diagnosisRecordRepository.count() != 0) {
            cnt = diagnosisRecordRepository.findTopByOrderByIdDesc().getId() + 1;
        } else {
            cnt = 1L;
        }

        imgCode.append(cnt); //저장 이미지 파일 명 설정

//        Optional<Member> optionalMember = memberRepository.findById(diagnosisDto.getUserId());
//        Member member = optionalMember.get(); //이미지 경로를 위한 member

        BufferedImage image = fileService.handleFileUpload(file, member.getName(), String.valueOf(imgCode));//이미지 저장

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://3.35.146.68:5000/predict";

        HttpHeaders header = new HttpHeaders();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("cropImage", file);
        body.add("cropType", diagnosisDto.getCropType());
        body.add("cropImageId", imgCode);
//        String requestBody = "{}";

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, header);

        ResponseEntity<AIResponse> responseEntity = restTemplate.exchange(url
                , HttpMethod.POST, entity, AIResponse.class);

        // 응답 처리
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            AIResponse responseBody = responseEntity.getBody();
            // 응답 처리 로직 작성
        } else {
            // 에러 처리 로직 작성
        }

        /*

         */

        AIResponse aiResponse = responseEntity.getBody();

        StringBuilder imagePath = new StringBuilder();
//        imagePath.append("http://15.164.23.13:8080/image/");
        imagePath.append("http://localhost:8080/image/");
        imagePath.append(member.getName());
        imagePath.append("/");
        imagePath.append(imgCode);

        Category category = categoryRepository.findByNameAndUserId("unclassified", member.getId());

        if (category == null) {
            Exception e = new Exception("실패");
            return ServiceResult.fail(e.getMessage(), e.getMessage());
        }

        DiagnosisRecord diagnosisRecord = DiagnosisRecord.builder()
                .userId(member.getId())
                .userLatitude(diagnosisDto.getUserLatitude())
                .userLongitude(diagnosisDto.getUserLongitude())
                .regDate(LocalDateTime.now())
                .cropType(diagnosisDto.getCropType())
                .imagePath(imagePath.toString())
                .categoryId(category.getId())
                .build();

        diagnosisRecordRepository.save(diagnosisRecord);

        DiagnosisResult diagnosisResult1 = DiagnosisResult.builder()
                .responseCode(aiResponse.getResponseCode())
                .diagnosisRecord(diagnosisRecord)
                .diseaseCode(aiResponse.getDiagnosisResult().get(0).getDiseaseCode())
                .accuracy(aiResponse.getDiagnosisResult().get(0).getAccuracy())
                .boxX1(aiResponse.getDiagnosisResult().get(0).getBoxX1())
                .boxX2(aiResponse.getDiagnosisResult().get(0).getBoxX2())
                .boxY1(aiResponse.getDiagnosisResult().get(0).getBoxY1())
                .boxY2(aiResponse.getDiagnosisResult().get(0).getBoxY2())
                .build();

        DiagnosisResult diagnosisResult2 = DiagnosisResult.builder()
                .responseCode(1)
                .diagnosisRecord(diagnosisRecord)
                .diseaseCode(aiResponse.getDiagnosisResult().get(1).getDiseaseCode())
                .accuracy(aiResponse.getDiagnosisResult().get(1).getAccuracy())
                .boxX1(aiResponse.getDiagnosisResult().get(1).getBoxX1())
                .boxX2(aiResponse.getDiagnosisResult().get(1).getBoxX2())
                .boxY1(aiResponse.getDiagnosisResult().get(1).getBoxY1())
                .boxY2(aiResponse.getDiagnosisResult().get(1).getBoxY2())
                .build();

        DiagnosisResult diagnosisResult3 = DiagnosisResult.builder()
                .responseCode(1)
                .diagnosisRecord(diagnosisRecord)
                .build();

        DiagnosisResponse diagnosisResponse = DiagnosisResponse.builder()
                .responseCode(1)
                .cropType(diagnosisDto.getCropType())
                .regDate(diagnosisDto.getRegDate())
                .diagnosisResults(List.of(DiagnosisOutput.to(diagnosisResult1), DiagnosisOutput.to(diagnosisResult2), DiagnosisOutput.to(diagnosisResult3)))
                .imagePath(imagePath.toString())
                .build();

        diagnosisResultRepository.save(diagnosisResult1);
        diagnosisResultRepository.save(diagnosisResult2);
        diagnosisResultRepository.save(diagnosisResult3);

        return ServiceResult.success(diagnosisResponse);
    }
}
