package com.example.servertest.main.nabatbu.inquiry.controller;

import com.example.servertest.main.global.model.ResponseResult;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.nabatbu.inquiry.model.request.RequestInquiry;
import com.example.servertest.main.nabatbu.inquiry.model.request.RequestReply;
import com.example.servertest.main.nabatbu.inquiry.service.InquiryService;
import com.example.servertest.main.nabatbu.inquiry.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crop/reply")
public class ReplyController {

    private final ReplyService replyService;

    @Operation(summary = "전문가 답변 등록")
    @PostMapping("/register")
    public ResponseEntity<?> registerInquiry(@RequestHeader("Authorization") String token, @RequestBody @Valid RequestReply requestReply) {

        ServiceResult result = replyService.register(token, requestReply);
        return ResponseResult.result(result);
    }

    @Operation(summary = "사용자 자신의 답변 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<?> getReplyList(@RequestHeader("Authorization") String token) {

        ServiceResult result = replyService.returnReplyList(token);
        return ResponseResult.result(result);
    }

    @Operation(summary = "전문가 자신의 답변 상세 조회")
    @GetMapping("/{replyId}")
    public ResponseEntity<?> getReplyDetail(@RequestHeader("Authorization") String token, @PathVariable Long replyId) {

        ServiceResult result = replyService.getReplyDetail(token, replyId);
        return ResponseResult.result(result);
    }

    @Operation(summary = "사용자 자신의 답변 내역 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReply(@RequestHeader("Authorization") String token, @RequestParam Long replyId) {

        ServiceResult result = replyService.deleteReply(token, replyId);
        return ResponseResult.result(result);
    }

    @Operation(summary = "사용자 자신의 답변 내역 수정")
    @DeleteMapping("/update")
    public ResponseEntity<?> updateReply(@RequestHeader("Authorization") String token, @RequestParam Long replyId, @RequestParam String contents) {

        ServiceResult result = replyService.update(token, replyId, contents);
        return ResponseResult.result(result);
    }
}
