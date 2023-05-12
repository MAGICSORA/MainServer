package com.example.servertest.main.crop.service;

import com.example.servertest.main.crop.entity.*;
import com.example.servertest.main.crop.exception.CropError;
import com.example.servertest.main.crop.exception.CropException;
import com.example.servertest.main.crop.model.request.DiagnosisDto;
import com.example.servertest.main.crop.model.response.DiagnosisItem;
import com.example.servertest.main.crop.model.response.DiagnosisResponse;
import com.example.servertest.main.crop.model.request.SickListDto;
import com.example.servertest.main.crop.model.response.ResponseDiagnosisRecord;
import com.example.servertest.main.crop.repository.*;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.member.entity.Member;
import com.example.servertest.main.member.exception.MemberError;
import com.example.servertest.main.member.exception.MemberException;
import com.example.servertest.main.member.repository.MemberRepository;
import com.example.servertest.main.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaBatBuService {

    private final SickListRepository sickListRepository;
    private final DiagnosisRecordRepository diagnosisRecordRepository;
    private final DiagnosisResultRepository diagnosisResultRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;
    private final MemberService memberService;
    private final DiseaseDetailRepository diseaseDetailRepository;
    private final CategoryRepository categoryRepository;

    public SickList saveSickList(SickListDto sickListDto) {
        SickList sickList = SickList.builder()
                .sickKey(sickListDto.getSickKey())
                .cropName(sickListDto.getCropName())
                .sickNameEng(sickListDto.getSickNameEng())
                .sickNameKor(sickListDto.getSickNameKor())
                .build();

        return sickListRepository.save(sickList);
    }

    public List<SickList> getSickList(String cropName, String sickNameKor) {
        List<SickList> sickListDtoList = sickListRepository.findByCropNameContainingAndSickNameKorContaining(
                cropName, sickNameKor);
        return sickListDtoList;
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

        System.out.println("테스트1");
        //테스트1


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

        DiagnosisItem tmpDiagnosisItem1 = DiagnosisItem.builder()
                .diseaseCode(0)
                .accuracy(0.81F)
                .boxX1(0.5F)
                .boxX2(0.5F)
                .boxY1(0.5F)
                .boxY2(0.5F)
                .build();

        DiagnosisItem tmpDiagnosisItem2 = DiagnosisItem.builder()
                .diseaseCode(2)
                .accuracy(0.86F)
                .boxX1(0.5F)
                .boxX2(0.5F)
                .boxY1(0.5F)
                .boxY2(0.5F)
                .build();

        DiagnosisItem tmpDiagnosisItem3 = new DiagnosisItem();

        List<DiagnosisItem> list = new ArrayList<>();
        list.add(tmpDiagnosisItem1);
        list.add(tmpDiagnosisItem2);
        list.add(tmpDiagnosisItem3);

        String str = new Gson().toJson(list);

        DiagnosisResult diagnosisResult =
                DiagnosisResult.builder()
                        .responseCode(1)
                        .diagnosisItems(str)
                        .build();

        diagnosisResultRepository.save(diagnosisResult);

        System.out.println("테스트2");
        //테스트1

        StringBuilder imagePath = new StringBuilder();
        imagePath.append("http://15.164.23.13:8080/image/");
        imagePath.append(member.getName());
        imagePath.append("/");
        imagePath.append(imgCode);

        DiagnosisResponse diagnosisResponse = DiagnosisResponse.builder()
                .responseCode(diagnosisResult.getResponseCode())
                .cropType(diagnosisDto.getCropType())
                .regDate(diagnosisDto.getRegDate())
                .diagnosisItems(List.of(tmpDiagnosisItem1, tmpDiagnosisItem2))
                .imagePath(imagePath.toString())
                .build();

        Category category = categoryRepository.findByNameAndUserId("unclassified", member.getId());

        if (category == null) {
            Exception e = new Exception("실패");
            return ServiceResult.fail(e.getMessage(), e.getMessage());
        }

        diagnosisRecordRepository.save(DiagnosisRecord.builder()
                .userId(member.getId())
                .diagnosisResultId(diagnosisResult.getId())
                .userLatitude(diagnosisDto.getUserLatitude())
                .userLongitude(diagnosisDto.getUserLongitude())
                .regDate(diagnosisDto.getRegDate())
                .cropType(diagnosisDto.getCropType())
                .imagePath(imagePath.toString())
                .categoryId(category.getId())
                .build());

//        DiagnosisResponse diagnosisResponse = DiagnosisResponse.builder()
//                .responseCode()
//                .cropType()
//                .regDate()
//                .diagnosisResult().build();

        return ServiceResult.success(diagnosisResponse);
    }

    public ServiceResult getDiagnosisRecord(Long diagnosisRecordId) throws JsonProcessingException {

        Optional<DiagnosisRecord> optionalDiagnosisRecord = diagnosisRecordRepository.findById(diagnosisRecordId);
        DiagnosisRecord diagnosisRecord = optionalDiagnosisRecord.get();
        long diagnosisResultId = diagnosisRecord.getDiagnosisResultId();

        Optional<DiagnosisResult> optionalDiagnosisResult = diagnosisResultRepository.findById(diagnosisResultId);
        DiagnosisResult diagnosisResult = optionalDiagnosisResult.get();

        String diagnosisItems = diagnosisResult.getDiagnosisItems();
        diagnosisItems = diagnosisItems.replace("DiagnosisItem", "");
        System.out.println(diagnosisItems);

        ObjectMapper objectMapper = new ObjectMapper();
        DiagnosisItem[] items = objectMapper.readValue(diagnosisItems, DiagnosisItem[].class);

        ResponseDiagnosisRecord result = ResponseDiagnosisRecord.builder()
                .userLatitude(diagnosisRecord.getUserLatitude())
                .userLongitude(diagnosisRecord.getUserLongitude())
                .regDate(diagnosisRecord.getRegDate())
                .diagnosisItems(items)
                .userId((int) diagnosisRecord.getUserId())
                .imagePath(diagnosisRecord.getImagePath())
                .cropType(diagnosisRecord.getCropType())
                .build();

        return ServiceResult.success(result);
    }

    public ServiceResult getSickDetail(int cropCode) {

        Optional<DiseaseDetail> optionalDiseaseDetail = diseaseDetailRepository.findById((long) cropCode);
        if (!optionalDiseaseDetail.isPresent()) {
            CropException e = new CropException(CropError.DISEASE_NOT_FOUND);
            return ServiceResult.fail(String.valueOf(e.getCropError()), e.getMessage());
        }

        DiseaseDetail diseaseDetail = optionalDiseaseDetail.get();
        return ServiceResult.success(diseaseDetail);
    }

    public ServiceResult getNearDiseases(float latitude, float longitude, String token) {

        try {
            Member member = memberService.validateMember(token);
        } catch (MemberException e) {
            return ServiceResult.fail(String.valueOf(e.getMemberError()), e.getMessage());
        }

        List<DiagnosisRecord> diagnosisRecordList1 = diagnosisRecordRepository.findAllByUserLatitudeBetween(latitude - 1, latitude + 1);
        List<DiagnosisRecord> diagnosisRecordList2 = diagnosisRecordRepository.findAllByUserLongitudeBetween(longitude - 1, longitude + 1);
        diagnosisRecordList1.addAll(diagnosisRecordList2);

        Set<DiagnosisRecord> diagnosisRecordSet = new HashSet<>(diagnosisRecordList1);
        return ServiceResult.success(diagnosisRecordSet);
    }
}
