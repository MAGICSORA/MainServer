package com.example.servertest.main.crop.service;

import com.example.servertest.main.crop.entity.DiagnosisRecord;
import com.example.servertest.main.crop.entity.DiagnosisResultFromModel;
import com.example.servertest.main.crop.entity.SickList;
import com.example.servertest.main.crop.model.*;
import com.example.servertest.main.crop.repository.DiagnosisRecordRepository;
import com.example.servertest.main.crop.repository.DiagnosisResultFromModelRepository;
import com.example.servertest.main.crop.repository.SickListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaBatBuService {

    private final SickListRepository sickListRepository;
    private final DiagnosisRecordRepository diagnosisRecordRepository;
    private final DiagnosisResultFromModelRepository diagnosisResultFromModelRepository;
    private final FileService fileService;

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

    public DiagnosisResponse returnDiagnosisResult(DiagnosisDto diagnosisDto, MultipartFile file) throws IOException {

        StringBuilder imgCode = new StringBuilder();
        imgCode.append(diagnosisDto.getUserId());
        imgCode.append("-");

        Long cnt;
        if(diagnosisRecordRepository.count()!=0) {
            cnt = diagnosisRecordRepository.findTopByOrderByIdDesc().getId() + 1;
        }else{
            cnt = 1L;
        }
        imgCode.append(cnt);

        BufferedImage image = fileService.handleFileUpload(file, "테스트유저1", String.valueOf(imgCode));

        //DiagnosisResultFromModel diagnosisResultFromModel = flaskService.call(imageCode, diagnosisDto.getCropType(), image);

        DiagnosisResult tmpDiagnosisResult1 = DiagnosisResult.builder()
                .diseaseCode(0)
                .accuracy(0.81F)
                .bBox(new float[]{0.5F, 0.5F, 0.7F, 0.65F}).build();

        DiagnosisResult tmpDiagnosisResult2 = DiagnosisResult.builder()
                .diseaseCode(2)
                .accuracy(0.86F)
                .bBox(new float[]{0.2F, 0.3F, 0.4F, 0.5F}).build();

        DiagnosisResultFromModel diagnosisResultFromModel =
                DiagnosisResultFromModel.builder()
                        .responseCode(1)
                        .diagnosisResults(List.of(tmpDiagnosisResult1, tmpDiagnosisResult2).toString())
                        .build();

        diagnosisResultFromModelRepository.save(diagnosisResultFromModel);

        DiagnosisResponse diagnosisResponse = DiagnosisResponse.builder()
                .responseCode(diagnosisResultFromModel.getResponseCode())
                .cropType(diagnosisDto.getCropType())
                .regDate(diagnosisDto.getRegDate())
                .diagnosisResults(List.of(tmpDiagnosisResult1, tmpDiagnosisResult2))
                .build();

        diagnosisRecordRepository.save(DiagnosisRecord.builder()
                .userId(diagnosisDto.getUserId())
                .diagnosisResultId(diagnosisResultFromModel.getId())
                .userLatitude(diagnosisDto.getUserLatitude())
                .userLongitude(diagnosisDto.getUserLongitude())
                .regDate(diagnosisDto.getRegDate())
                .cropType(diagnosisDto.getCropType())
                .imagePath(imgCode.toString())
                .build());

//        DiagnosisResponse diagnosisResponse = DiagnosisResponse.builder()
//                .responseCode()
//                .cropType()
//                .regDate()
//                .diagnosisResult().build();

        return diagnosisResponse;
    }
}
