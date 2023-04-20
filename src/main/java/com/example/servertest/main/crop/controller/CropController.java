package com.example.servertest.main.crop.controller;

import com.example.servertest.main.crop.entity.SickList;
import com.example.servertest.main.crop.model.request.DiagnosisDto;
import com.example.servertest.main.crop.model.response.DiagnosisResponse;
import com.example.servertest.main.crop.model.request.SickListDto;
import com.example.servertest.main.crop.service.NaBatBuService;
import com.example.servertest.main.crop.type.DiseaseCode;
import com.example.servertest.main.global.model.ResponseResult;
import com.example.servertest.main.psis.component.PsisManager;
import com.example.servertest.main.psis.model.request.RequestPsisInfo;
import com.example.servertest.main.psis.model.request.RequestPsisList;
import com.example.servertest.main.psis.service.PsisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crop")
public class CropController {

    private final NaBatBuService naBatBuService;
    private final PsisManager psisManager;
    private final PsisService psisService;

    @PostMapping("/input/sickList")
    public ResponseEntity<?> inputSickList(
            @RequestBody SickListDto sickListDto) {
        SickList sickList = naBatBuService.saveSickList(sickListDto);

        return ResponseEntity.ok(sickList);
    }

    @GetMapping("/find/sickList")
    public ResponseEntity<?> sickList(@RequestParam String cropName,
                                      @RequestParam String sickNameKor) {

        List<SickList> sickList = naBatBuService.getSickList(cropName,
                sickNameKor);

        return ResponseEntity.ok(sickList);
    }

    @GetMapping("/sickDetail") //병 상세정보 조회
    public ResponseEntity<?> sickDetail(@RequestParam int cropCode) {

        return null;
    }

    @GetMapping("/psisList") //농약 리스트 조회
    public Map<String, ?> krxParser2(@RequestBody RequestPsisList request) throws IOException {
        String urlBuilder = psisManager.makePsisListRequestUrl(request.getCropName(), request.getDiseaseWeedName(), request.getDisplayCount(), request.getStartPoint());

        return psisService.returnResult(urlBuilder, true);
    }

    @GetMapping("/psisDetail") //농약 상세정보 조회
    public Map<String, ?> krxParser3(@RequestBody RequestPsisInfo request) throws IOException {
        String urlBuilder = psisManager.makePsisInfoRequestUrl(request.getPestiCode(), request.getDiseaseUseSeq(), request.getDisplayCount(), request.getStartPoint());

        return psisService.returnResult(urlBuilder, false);
    }

    @GetMapping("/diagnosis") //진단 요청
    public ResponseEntity<?> returnDiagnosis(
            @RequestPart(value = "requestInput") DiagnosisDto diagnosisDto
            , @RequestPart(value = "image") MultipartFile file) throws IOException {
        DiagnosisResponse diagnosisResponse = naBatBuService.returnDiagnosisResult(diagnosisDto, file);

        return ResponseEntity.ok(diagnosisResponse);
    }

    @GetMapping("/diagnosisRecord") //사용자 진단 기록 조회
    public ResponseEntity<?> diagnosisRecord(@RequestParam Long diagnosisRecordId) throws JsonProcessingException {

        return ResponseResult.result(naBatBuService.getDiagnosisRecord(diagnosisRecordId));
    }
}