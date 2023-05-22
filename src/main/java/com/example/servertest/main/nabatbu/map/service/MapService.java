package com.example.servertest.main.nabatbu.map.service;

import com.example.servertest.main.nabatbu.category.repository.CategoryRepository;
import com.example.servertest.main.nabatbu.category.repository.MyCropHistoryRepository;
import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisRecord;
import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisResult;
import com.example.servertest.main.nabatbu.diagnosis.repository.DiagnosisRecordRepository;
import com.example.servertest.main.nabatbu.diagnosis.repository.DiagnosisResultRepository;
import com.example.servertest.main.nabatbu.diagnosis.repository.SickListRepository;
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

    private final SickListRepository sickListRepository;
    private final DiagnosisRecordRepository diagnosisRecordRepository;
    private final DiagnosisResultRepository diagnosisResultRepository;
    private final FileService fileService;
    private final MemberService memberService;
    private final CategoryRepository categoryRepository;
    private final MyCropHistoryRepository myCropHistoryRepository;

    public ServiceResult getNearDiseases(MapRequest mapRequest, String token) {

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
