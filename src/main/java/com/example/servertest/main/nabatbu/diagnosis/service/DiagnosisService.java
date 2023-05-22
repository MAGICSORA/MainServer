package com.example.servertest.main.nabatbu.diagnosis.service;

import com.example.servertest.main.nabatbu.category.repository.CategoryRepository;
import com.example.servertest.main.nabatbu.category.repository.MyCropHistoryRepository;
import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisRecord;
import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisResult;
import com.example.servertest.main.nabatbu.diagnosis.model.request.DiagnosisDto;
import com.example.servertest.main.nabatbu.diagnosis.model.response.DiagnosisOutput;
import com.example.servertest.main.nabatbu.diagnosis.model.response.DiagnosisResponse;
import com.example.servertest.main.nabatbu.diagnosis.model.response.ResponseDiagnosisRecord;
import com.example.servertest.main.nabatbu.category.entity.Category;
import com.example.servertest.main.nabatbu.category.entity.MyCropHistory;
import com.example.servertest.main.nabatbu.diagnosis.repository.DiagnosisRecordRepository;
import com.example.servertest.main.nabatbu.diagnosis.repository.DiagnosisResultRepository;
import com.example.servertest.main.nabatbu.diagnosis.repository.SickListRepository;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.nabatbu.member.entity.Member;
import com.example.servertest.main.nabatbu.member.exception.MemberError;
import com.example.servertest.main.nabatbu.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    private final SickListRepository sickListRepository;
    private final DiagnosisRecordRepository diagnosisRecordRepository;
    private final DiagnosisResultRepository diagnosisResultRepository;
    private final FileService fileService;
    private final MemberService memberService;
    private final CategoryRepository categoryRepository;
    private final MyCropHistoryRepository myCropHistoryRepository;

    public ServiceResult returnDiagnosisResult(DiagnosisDto diagnosisDto, MultipartFile file, String token) throws IOException {

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

        //DiagnosisResultFromModel diagnosisResultFromModel = flaskService.request(imageCode, diagnosisDto.getCropType(), image);
        //진단 요청

        StringBuilder imagePath = new StringBuilder();
        imagePath.append("http://15.164.23.13:8080/image/");
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
                .responseCode(1)
                .diagnosisRecord(diagnosisRecord)
                .diseaseCode(0)
                .accuracy(0.81F)
                .boxX1(0.5F)
                .boxX2(0.5F)
                .boxY1(0.5F)
                .boxY2(0.5F)
                .build();

        DiagnosisResult diagnosisResult2 = DiagnosisResult.builder()
                .responseCode(1)
                .diagnosisRecord(diagnosisRecord)
                .diseaseCode(2)
                .accuracy(0.86F)
                .boxX1(0.5F)
                .boxX2(0.5F)
                .boxY1(0.5F)
                .boxY2(0.5F)
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

        List<DiagnosisOutput> diagnosisOutputs = new ArrayList<>();
        for (DiagnosisResult diagnosisResult : diagnosisResults) {
            diagnosisOutputs.add(DiagnosisOutput.to(diagnosisResult));
        }

//        String diagnosisItems = diagnosisResult.getDiagnosisItems();
//        diagnosisItems = diagnosisItems.replace("DiagnosisItem", "");
//        System.out.println(diagnosisItems);

//        ObjectMapper objectMapper = new ObjectMapper();
//        DiagnosisItemtmp[] items = objectMapper.readValue(diagnosisItems, DiagnosisItemtmp[].class);

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
