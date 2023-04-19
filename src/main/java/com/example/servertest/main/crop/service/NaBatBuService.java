package com.example.servertest.main.crop.service;

import com.example.servertest.main.crop.entity.DiagnosisRecord;
import com.example.servertest.main.crop.entity.DiagnosisResult;
import com.example.servertest.main.crop.entity.SickList;
import com.example.servertest.main.crop.model.DiagnosisDto;
import com.example.servertest.main.crop.model.DiagnosisItem;
import com.example.servertest.main.crop.model.DiagnosisResponse;
import com.example.servertest.main.crop.model.SickListDto;
import com.example.servertest.main.crop.model.response.ResponseDiagnosisRecord;
import com.example.servertest.main.crop.repository.DiagnosisRecordRepository;
import com.example.servertest.main.crop.repository.DiagnosisResultRepository;
import com.example.servertest.main.crop.repository.SickListRepository;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.member.entity.Member;
import com.example.servertest.main.member.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaBatBuService {

    private final SickListRepository sickListRepository;
    private final DiagnosisRecordRepository diagnosisRecordRepository;
    private final DiagnosisResultRepository diagnosisResultRepository;
    private final MemberRepository memberRepository;
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
        if (diagnosisRecordRepository.count() != 0) {
            cnt = diagnosisRecordRepository.findTopByOrderByIdDesc().getId() + 1;
        } else {
            cnt = 1L;
        }
        imgCode.append(cnt);

        Optional<Member> optionalMember = memberRepository.findById(diagnosisDto.getUserId());
        Member member = optionalMember.get();

        BufferedImage image = fileService.handleFileUpload(file, member.getName(), String.valueOf(imgCode));

        //DiagnosisResultFromModel diagnosisResultFromModel = flaskService.request(imageCode, diagnosisDto.getCropType(), image);

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

        StringBuilder imagePath = new StringBuilder();
        imagePath.append("http://localhost:8080/image/");
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

        diagnosisRecordRepository.save(DiagnosisRecord.builder()
                .userId(diagnosisDto.getUserId())
                .diagnosisResultId(diagnosisResult.getId())
                .userLatitude(diagnosisDto.getUserLatitude())
                .userLongitude(diagnosisDto.getUserLongitude())
                .regDate(diagnosisDto.getRegDate())
                .cropType(diagnosisDto.getCropType())
                .imagePath(imagePath.toString())
                .build());

//        DiagnosisResponse diagnosisResponse = DiagnosisResponse.builder()
//                .responseCode()
//                .cropType()
//                .regDate()
//                .diagnosisResult().build();

        return diagnosisResponse;
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
}
