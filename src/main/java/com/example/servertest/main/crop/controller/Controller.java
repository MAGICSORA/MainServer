package com.example.servertest.main.crop.controller;

import com.example.servertest.main.crop.entity.SickList;
import com.example.servertest.main.crop.model.DiagnosisDto;
import com.example.servertest.main.crop.model.DiagnosisResponse;
import com.example.servertest.main.crop.model.SickListDto;
import com.example.servertest.main.crop.service.NaBatBuService;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final NaBatBuService naBatBuService;

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

    @GetMapping("/find/sickDetail")
    public ResponseEntity<?> sickDetail(@RequestParam String sickKey) {

        return null;
        //농촌진흥청 병 상세 정보
    }

    @GetMapping("/find/pesticide")
    public ResponseEntity<?> getPesticide(@RequestParam String sickKey) {

        return null;
        //농촌진흥청 농약 리스트
    }

    @GetMapping("/find/pesticideDetail")
    public ResponseEntity<?> getPesticideDetail(
            @RequestParam String pesticideKey) {

        return null;
        //농촌진흥청 농약 상세 정보
    }

    @GetMapping("/diagnosis")
    public ResponseEntity<?> returnDiagnosis(
            @RequestPart(value = "dto") DiagnosisDto diagnosisDto
            , @RequestPart(value = "image") MultipartFile file) throws IOException {
        DiagnosisResponse diagnosisResponse = naBatBuService.returnDiagnosisResult(diagnosisDto, file);

        return ResponseEntity.ok(diagnosisResponse);
    }
}