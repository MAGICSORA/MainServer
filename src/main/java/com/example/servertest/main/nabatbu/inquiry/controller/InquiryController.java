package com.example.servertest.main.nabatbu.inquiry.controller;

import com.example.servertest.main.global.model.ResponseResult;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.nabatbu.inquiry.model.request.RequestInquiry;
import com.example.servertest.main.nabatbu.inquiry.service.InquiryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crop/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    @Operation(summary = "문의 등록")
    @PostMapping("/register")
    public ResponseEntity<?> registerInquiry(@RequestHeader("Authorization") String token, @RequestBody @Valid RequestInquiry requestInquiry) {

        ServiceResult result = inquiryService.register(token, requestInquiry);
        return ResponseResult.result(result);
    }

    @Operation(summary = "사용자 자신의 문의 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<?> getInquiryList(@RequestHeader("Authorization") String token) {

        ServiceResult result = inquiryService.returnInquiryList(token);
        return ResponseResult.result(result);
    }

    @Operation(summary = "사용자 자신의 문의 상세 조회")
    @GetMapping("/{inquiryId}")
    public ResponseEntity<?> getInquiryDetail(@RequestHeader("Authorization") String token, @PathVariable Long inquiryId) {

        ServiceResult result = inquiryService.getInquiryDetail(token, inquiryId);
        return ResponseResult.result(result);
    }

    @Operation(summary = "사용자 자신의 문의 내역 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteInquiry(@RequestHeader("Authorization") String token, @RequestParam Long inquiryId) {

        ServiceResult result = inquiryService.deleteInquiry(token, inquiryId);
        return ResponseResult.result(result);
    }
}
