package com.example.servertest.main.nabatbu.map.service;

import com.example.servertest.main.nabatbu.category.repository.CategoryRepository;
import com.example.servertest.main.nabatbu.category.repository.MyCropHistoryRepository;
import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisRecord;
import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisResult;
import com.example.servertest.main.nabatbu.diagnosis.repository.DiagnosisRecordRepository;
import com.example.servertest.main.nabatbu.diagnosis.repository.DiagnosisResultRepository;
import com.example.servertest.main.nabatbu.cropInfo.repository.SickListRepository;
import com.example.servertest.main.nabatbu.diagnosis.service.FileService;
import com.example.servertest.main.nabatbu.map.model.request.MapRequest;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.nabatbu.member.entity.Member;
import com.example.servertest.main.nabatbu.member.exception.MemberError;
import com.example.servertest.main.nabatbu.member.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class MapService {

    private final MemberService memberService;

    private final DiagnosisRecordRepository diagnosisRecordRepository;
    private final DiagnosisResultRepository diagnosisResultRepository;

    public ServiceResult getNearDiseases(MapRequest mapRequest, String token) {
//        System.out.println(mapRequest);

        Member member = new Member();
        try {
            member = memberService.validateMember(token);
        } catch (ExpiredJwtException e) {
            MemberError error = MemberError.EXPIRED_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        double latitude = mapRequest.getLatitude();
        double longitude = mapRequest.getLongitude();

        List<DiagnosisRecord> diagnosisRecordList1 = diagnosisRecordRepository.findAllByUserLatitudeBetween(latitude - 0.1099585, latitude + 0.1099585);
        List<DiagnosisRecord> diagnosisRecordList2 = diagnosisRecordRepository.findAllByUserLongitudeBetween(longitude - 0.08874, longitude + 0.08874);
        diagnosisRecordList1.addAll(diagnosisRecordList2);

        Set<DiagnosisRecord> diagnosisRecordSet = new HashSet<>(diagnosisRecordList1);

//        System.out.println(diagnosisRecordSet.size());
//        System.out.println(mapRequest.getDate());

        List<DiagnosisResult> diagnosisResultList = new ArrayList<>();

        for (DiagnosisRecord item : diagnosisRecordSet) {
            if (item.getRegDate().isBefore(mapRequest.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())) {
//                System.out.println(item.getId());
                continue;
            }
//            System.out.println(item.getCropType());
            List<DiagnosisResult> diagnosisResults = diagnosisResultRepository.findAllByDiagnosisRecord(item);
//            System.out.println(diagnosisResults.size());

            if (item.getCropType() == 0){
                if(!mapRequest.getMapSheepCropList().get(0).getIsOn()){
//                    System.out.println(mapRequest.getMapSheepCropList().get(0).getIsOn());
//                    System.out.println("isOff");
                    continue;
                }else {
                    for (DiagnosisResult result : diagnosisResults) {
                        if (result.getAccuracy() >= mapRequest.getMapSheepCropList().get(0).getAccuracy()) {
//                            System.out.println(result);
                            diagnosisResultList.add(result);
                        }
//                        System.out.println("lower accuracy");
                    }
                }
            } else if (item.getCropType() == 1){
                if(!mapRequest.getMapSheepCropList().get(1).getIsOn()){
                    continue;
                }else {
                    for (DiagnosisResult result : diagnosisResults) {
                        if (result.getAccuracy() >= mapRequest.getMapSheepCropList().get(1).getAccuracy()) {
                            diagnosisResultList.add(result);
                        }
                    }
                }
            } else if (item.getCropType() == 2){
                if(!mapRequest.getMapSheepCropList().get(2).getIsOn()){
                    continue;
                }else {
                    for (DiagnosisResult result : diagnosisResults) {
                        if (result.getAccuracy() >= mapRequest.getMapSheepCropList().get(2).getAccuracy()) {
                            diagnosisResultList.add(result);
                        }
                    }
                }
            } else if (item.getCropType() == 3){
                if(!mapRequest.getMapSheepCropList().get(3).getIsOn()){
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

        return ServiceResult.success(diagnosisResultList);
    }
}
