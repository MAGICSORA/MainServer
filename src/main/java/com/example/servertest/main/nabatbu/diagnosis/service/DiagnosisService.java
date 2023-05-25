package com.example.servertest.main.nabatbu.diagnosis.service;

import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.nabatbu.category.entity.Category;
import com.example.servertest.main.nabatbu.category.entity.MyCropHistory;
import com.example.servertest.main.nabatbu.category.repository.CategoryRepository;
import com.example.servertest.main.nabatbu.category.repository.MyCropHistoryRepository;
import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisRecord;
import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisResult;
import com.example.servertest.main.nabatbu.diagnosis.model.request.DiagnosisDto;
import com.example.servertest.main.nabatbu.diagnosis.model.request.DiagnosisRequest;
import com.example.servertest.main.nabatbu.diagnosis.model.response.DiagnosisOutput;
import com.example.servertest.main.nabatbu.diagnosis.model.response.DiagnosisResponse;
import com.example.servertest.main.nabatbu.diagnosis.model.response.DiagnosisResultOutput;
import com.example.servertest.main.nabatbu.diagnosis.model.response.ResponseDiagnosisRecord;
import com.example.servertest.main.nabatbu.diagnosis.repository.DiagnosisRecordRepository;
import com.example.servertest.main.nabatbu.diagnosis.repository.DiagnosisResultRepository;
import com.example.servertest.main.nabatbu.member.entity.Member;
import com.example.servertest.main.nabatbu.member.exception.MemberError;
import com.example.servertest.main.nabatbu.member.service.MemberService;
import com.example.servertest.main.test.model.AIResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiagnosisService {

    private final FileService fileService;
    private final MemberService memberService;

    private final DiagnosisRecordRepository diagnosisRecordRepository;
    private final DiagnosisResultRepository diagnosisResultRepository;
    private final CategoryRepository categoryRepository;
    private final MyCropHistoryRepository myCropHistoryRepository;

    public ServiceResult request(DiagnosisDto diagnosisDto, MultipartFile file, String token) throws IOException {

        Member member;
        try {
            member = memberService.validateMember(token);
        } catch (ExpiredJwtException e) {
            MemberError error = MemberError.EXPIRED_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
//            e.printStackTrace();
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        StringBuilder imgCode = new StringBuilder();
        imgCode.append(member.getId());
        imgCode.append("-");

        Long cnt;
        if (diagnosisRecordRepository.count() != 0) {
            cnt = diagnosisRecordRepository.findTopByOrderByIdDesc().getId() + 1;
        } else {
            cnt = 1L;
        }
        imgCode.append(cnt);

        BufferedImage image = fileService.handleFileUpload(file, member.getEmail(), String.valueOf(imgCode));//이미지 저장

        DiagnosisRequest diagnosisRequest = DiagnosisRequest.builder().cropImageId(String.valueOf(imgCode)).cropType(diagnosisDto.getCropType()).build();

        String data = mapper.writeValueAsString(diagnosisRequest);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://3.35.146.68:5000/predict", getRequest(data, file), String.class);
        System.out.println(responseEntity.getBody().toString());
        System.out.println("test1");

        AIResponse aiResponse = mapper.readValue(responseEntity.getBody(),AIResponse.class);
        System.out.println("test2");
        System.out.println(aiResponse);

        StringBuilder imagePath = new StringBuilder();
        imagePath.append("http://15.164.23.13:8080/image/");
//        imagePath.append("http://localhost:8080/image/");
        imagePath.append(member.getEmail());
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
        List<DiagnosisOutput> diagnosisResults = aiResponse.getDiagnoseResults();
        List<DiagnosisResultOutput> outputList = new ArrayList<>();

        for (DiagnosisOutput item : diagnosisResults) {

            DiagnosisResultOutput tmp = DiagnosisResultOutput.from(item);
            outputList.add(tmp);

            DiagnosisResult diagnosisResult = DiagnosisResult.builder()
                    .responseCode(aiResponse.getResponseCode())
                    .diagnosisRecord(diagnosisRecord)
                    .diseaseCode(item.getDiseaseCode())
                    .accuracy(item.getAccuracy())
                    .boxX1(item.getBbox().get(0))
                    .boxY1(item.getBbox().get(1))
                    .boxX2(item.getBbox().get(2))
                    .boxY2(item.getBbox().get(3))
                    .sickKey(tmp.getSickKey())
                    .build();
            diagnosisResultRepository.save(diagnosisResult);
        }

        DiagnosisResponse diagnosisResponse = DiagnosisResponse.builder()
                .diagnosisRecordId(diagnosisRecord.getId())
                .responseCode(1)
                .cropType(diagnosisDto.getCropType())
                .regDate(diagnosisDto.getRegDate())
                .diagnosisResults(outputList)
                .imagePath(imagePath.toString())
                .build();

        return ServiceResult.success(diagnosisResponse);
    }

    private HttpEntity<MultiValueMap<String, Object>> getRequest(final String data, final MultipartFile file) throws IOException {

        ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
            // 기존 ByteArrayResource의 getFilename 메서드 override
            @Override
            public String getFilename() {
                return "requestFile.wav";
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("data", data);
        body.add("file", fileResource);

        return new HttpEntity<>(body, headers);
    }

    public ServiceResult getDiagnosisRecord(Long diagnosisRecordId, String token) throws JsonProcessingException {

        Member member = new Member();
        try {
            member = memberService.validateMember(token);
        } catch (ExpiredJwtException e) {
            MemberError error = MemberError.EXPIRED_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
//            e.printStackTrace();
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        Optional<DiagnosisRecord> optionalDiagnosisRecord = diagnosisRecordRepository.findById(diagnosisRecordId);
        DiagnosisRecord diagnosisRecord = optionalDiagnosisRecord.get();

        List<DiagnosisResult> diagnosisResults = diagnosisResultRepository.findAllByDiagnosisRecord(diagnosisRecord);

        List<DiagnosisResultOutput> diagnosisOutputs = new ArrayList<>();

        for (DiagnosisResult diagnosisResult : diagnosisResults) {
            diagnosisOutputs.add(DiagnosisResultOutput.builder()
                    .diseaseCode(diagnosisResult.getDiseaseCode())
                    .sickKey(diagnosisResult.getSickKey())
                    .accuracy(diagnosisResult.getAccuracy())
                    .boxX1(diagnosisResult.getBoxX1())
                    .boxY1(diagnosisResult.getBoxY1())
                    .boxX2(diagnosisResult.getBoxX2())
                    .boxY2(diagnosisResult.getBoxY2())
                    .build());
        }
        ResponseDiagnosisRecord result = ResponseDiagnosisRecord.builder()
                .userLatitude(diagnosisRecord.getUserLatitude())
                .userLongitude(diagnosisRecord.getUserLongitude())
                .regDate(diagnosisRecord.getRegDate())
                .diagnosisResults(diagnosisOutputs)
                .userId((int) diagnosisRecord.getUserId())
                .imagePath(diagnosisRecord.getImagePath())
                .cropType(diagnosisRecord.getCropType())
                .build();

        return ServiceResult.success(result);
    }

    public ServiceResult deleteDiagnosisRecord(Long diagnosisRecordId, String token) {

        Member member = new Member();
        try {
            member = memberService.validateMember(token);
        } catch (ExpiredJwtException e) {
            MemberError error = MemberError.EXPIRED_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
//            e.printStackTrace();
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        Optional<DiagnosisRecord> optionalDiagnosisRecord = diagnosisRecordRepository.findById(diagnosisRecordId);
        DiagnosisRecord diagnosisRecord = optionalDiagnosisRecord.get();

        //myCropHistory 삭제
        List<MyCropHistory> myCropHistories = myCropHistoryRepository.findAllByDiagnosisRecord(diagnosisRecord);
        myCropHistoryRepository.deleteAll(myCropHistories);

        //diagnosisResult 삭제
        List<DiagnosisResult> diagnosisResults = diagnosisResultRepository.findAllByDiagnosisRecord(diagnosisRecord);
        diagnosisResultRepository.deleteAll(diagnosisResults);

        //diagnosisRecord 삭제
        diagnosisRecordRepository.delete(diagnosisRecord);

        return ServiceResult.success("성공");
    }
}
