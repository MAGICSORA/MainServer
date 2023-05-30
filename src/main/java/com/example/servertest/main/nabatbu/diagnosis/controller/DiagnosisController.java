package com.example.servertest.main.nabatbu.diagnosis.controller;

import com.example.servertest.main.nabatbu.diagnosis.model.request.DiagnosisDto;
import com.example.servertest.main.nabatbu.diagnosis.service.DiagnosisService;
import com.example.servertest.main.global.model.ResponseResult;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.test.service.TestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crop")
public class DiagnosisController {

    private final TestService testService;
    private final DiagnosisService diagnosisService;

    @Operation(summary = "진단요청")
    @PostMapping("/diagnosis") //진단 요청
    public ResponseEntity<?> request(@RequestPart(value = "requestInput") DiagnosisDto diagnosisDto
            , @RequestPart(value = "image") MultipartFile file, @RequestHeader("Authorization") String token) throws IOException {

        ServiceResult result = diagnosisService.request(diagnosisDto, file, token);

        return ResponseResult.result(result);

    }

    @Operation(summary = "사용자 진단 기록 리스트 조회")
    @GetMapping("/list/diagnosisRecord") //사용자 진단 기록 조회
    public ResponseEntity<?> diagnosisRecordList(@RequestHeader("Authorization") String token) {

        return ResponseResult.result(diagnosisService.getDiagnosisRecordList(token));
    }

    @Operation(summary = "사용자 진단 기록 조회")
    @GetMapping("/diagnosisRecord") //사용자 진단 기록 조회
    public ResponseEntity<?> diagnosisRecord(@RequestParam Long diagnosisRecordId, @RequestHeader("Authorization") String token) throws JsonProcessingException {

        return ResponseResult.result(diagnosisService.getDiagnosisRecord(diagnosisRecordId, token));
    }

    @Operation(summary = "사용자 진단 기록 삭제")
    @DeleteMapping("/diagnosisRecord/delete") //사용자 진단 기록 조회
    public ResponseEntity<?> deleteDiagnosisRecord(@RequestParam Long diagnosisRecordId, @RequestHeader("Authorization") String token) {

        return ResponseResult.result(diagnosisService.deleteDiagnosisRecord(diagnosisRecordId, token));
    }
}
