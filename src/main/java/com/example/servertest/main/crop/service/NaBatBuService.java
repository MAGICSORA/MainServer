package com.example.servertest.main.crop.service;

import com.example.servertest.main.crop.entity.Category;
import com.example.servertest.main.crop.entity.DiagnosisRecord;
import com.example.servertest.main.crop.entity.DiagnosisResult;
import com.example.servertest.main.crop.entity.SickList;
import com.example.servertest.main.crop.model.request.DiagnosisDto;
import com.example.servertest.main.crop.model.request.MapRequest;
import com.example.servertest.main.crop.model.request.SickListDto;
import com.example.servertest.main.crop.model.response.DiagnosisOutput;
import com.example.servertest.main.crop.model.response.DiagnosisResponse;
import com.example.servertest.main.crop.model.response.ResponseDiagnosisRecord;
import com.example.servertest.main.crop.repository.CategoryRepository;
import com.example.servertest.main.crop.repository.DiagnosisRecordRepository;
import com.example.servertest.main.crop.repository.DiagnosisResultRepository;
import com.example.servertest.main.crop.repository.SickListRepository;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.member.entity.Member;
import com.example.servertest.main.member.exception.MemberError;
import com.example.servertest.main.member.exception.MemberException;
import com.example.servertest.main.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaBatBuService {

    private final SickListRepository sickListRepository;
    private final DiagnosisRecordRepository diagnosisRecordRepository;
    private final DiagnosisResultRepository diagnosisResultRepository;
    private final FileService fileService;
    private final MemberService memberService;
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

    public ServiceResult getDiagnosisRecord(Long diagnosisRecordId) throws JsonProcessingException {

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

    public ServiceResult getNearDiseases(MapRequest mapRequest, String token) {

        Member member;
        try {
            member = memberService.validateMember(token);
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        /*
        0.1099585
        0.08874
        */
        double latitude = mapRequest.getLatitude();
        double longitude = mapRequest.getLongitude();

        List<DiagnosisRecord> diagnosisRecordList1 = diagnosisRecordRepository.findAllByUserLatitudeBetween(latitude - 0.1099585F, latitude + 0.1099585F);
        List<DiagnosisRecord> diagnosisRecordList2 = diagnosisRecordRepository.findAllByUserLongitudeBetween(longitude - 0.08874F, longitude + 0.08874F);
        diagnosisRecordList1.addAll(diagnosisRecordList2);

        Set<DiagnosisRecord> diagnosisRecordSet = new HashSet<>(diagnosisRecordList1);

        List<DiagnosisResult> diagnosisResultList = new ArrayList<>();

        for (DiagnosisRecord item : diagnosisRecordSet) {
            if (item.getRegDate().isBefore(mapRequest.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())) {
                continue;
            }
            List<DiagnosisResult> diagnosisResults = diagnosisResultRepository.findAllByDiagnosisRecord(item);

            if (item.getCropType() == 0){
                if(!mapRequest.getMapSheepCropList().get(0).isOn()){
                    continue;
                }else {
                    for (DiagnosisResult result : diagnosisResults) {
                        if (result.getAccuracy() >= mapRequest.getMapSheepCropList().get(0).getAccuracy()) {
                            diagnosisResultList.add(result);
                        }
                    }
                }
            } else if (item.getCropType() == 1){
                if(!mapRequest.getMapSheepCropList().get(1).isOn()){
                    continue;
                }else {
                    for (DiagnosisResult result : diagnosisResults) {
                        if (result.getAccuracy() >= mapRequest.getMapSheepCropList().get(1).getAccuracy()) {
                            diagnosisResultList.add(result);
                        }
                    }
                }
            } else if (item.getCropType() == 2){
                if(!mapRequest.getMapSheepCropList().get(2).isOn()){
                    continue;
                }else {
                    for (DiagnosisResult result : diagnosisResults) {
                        if (result.getAccuracy() >= mapRequest.getMapSheepCropList().get(2).getAccuracy()) {
                            diagnosisResultList.add(result);
                        }
                    }
                }
            } else if (item.getCropType() == 3){
                if(!mapRequest.getMapSheepCropList().get(3).isOn()){
                    continue;
                }else {
                    for (DiagnosisResult result : diagnosisResults) {
                        if (result.getAccuracy() >= mapRequest.getMapSheepCropList().get(3).getAccuracy()) {
                            diagnosisResultList.add(result);
                        }
                    }
                }
            }
        }

//        for (DiagnosisRecord item : diagnosisRecordSet) {
//            if (StaticMethod.distance(item.getUserLatitude(), item.getUserLongitude(), latitude, longitude) > 1000) {
//                diagnosisRecordSet.remove(item);
//            }
//        }
        return ServiceResult.success(diagnosisResultList);
    }
}
