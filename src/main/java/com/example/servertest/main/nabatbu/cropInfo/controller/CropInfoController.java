package com.example.servertest.main.nabatbu.cropInfo.controller;

import com.example.servertest.main.nabatbu.cropInfo.ncpms.component.NcpmsManager;
import com.example.servertest.main.nabatbu.cropInfo.ncpms.model.request.RequestNcpmsSick;
import com.example.servertest.main.nabatbu.cropInfo.ncpms.model.request.RequestNcpmsSickDetail;
import com.example.servertest.main.nabatbu.cropInfo.ncpms.service.NcpmsService;
import com.example.servertest.main.nabatbu.cropInfo.psis.component.PsisManager;
import com.example.servertest.main.nabatbu.cropInfo.psis.model.request.RequestPsisInfo;
import com.example.servertest.main.nabatbu.cropInfo.psis.model.request.RequestPsisList;
import com.example.servertest.main.nabatbu.cropInfo.psis.service.PsisService;
import com.example.servertest.main.nabatbu.category.service.CategoryService;
import com.example.servertest.main.nabatbu.category.service.MyCropHistoryService;
import com.example.servertest.main.nabatbu.cropInfo.exception.NcpmsError;
import com.example.servertest.main.nabatbu.cropInfo.exception.NcpmsException;
import com.example.servertest.main.nabatbu.cropInfo.service.CrawlingService;
import com.example.servertest.main.global.model.ResponseResult;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.test.service.TestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crop")
public class CropInfoController {

    private final PsisManager psisManager;
    private final PsisService psisService;
    private final NcpmsManager ncpmsManager;
    private final NcpmsService ncpmsService;
    private final TestService testService;
    private final CrawlingService crawlingService;
    private final CategoryService categoryService;
    private final MyCropHistoryService myCropHistoryService;

    @Operation(summary = "농약 리스트 조회")
    @GetMapping("/psisList") //농약 리스트 조회
    public ResponseEntity<?> krxParser2(@RequestBody RequestPsisList request, @RequestHeader("Authorization") String token) throws IOException {
        String urlBuilder = psisManager.makePsisListRequestUrl(request.getCropName(), request.getDiseaseWeedName(), request.getDisplayCount(), request.getStartPoint());

        System.out.println(urlBuilder);
        return ResponseResult.result(psisService.returnResult(urlBuilder, true, token));
    }

    @Operation(summary = "농약 상세정보 조회")
    @GetMapping("/psisDetail") //농약 상세정보 조회
    public ResponseEntity<?> krxParser3(@RequestBody RequestPsisInfo request, @RequestHeader("Authorization") String token) throws IOException {
        String urlBuilder = psisManager.makePsisInfoRequestUrl(request.getPestiCode(), request.getDiseaseUseSeq(), request.getDisplayCount(), request.getStartPoint());

        System.out.println(urlBuilder);
        return ResponseResult.result(psisService.returnResult(urlBuilder, false, token));
    }

    @Operation(summary = "메인페이지 병해발생정보 리스트 저장 (스케줄러 자동화)")
    @PutMapping("/save/noticeList")//메인페이지 병해발생정보 리스트 저장
    public void saveNoticeList() {

        String idx = crawlingService.getUrl(crawlingService.getIndex());

        crawlingService.updateAllData(idx);

//        System.out.println(crawlingService.getAllData(idx));
    }

    @Operation(summary = "메인페이지 병해발생정보 리스트 조회")
    @GetMapping("/noticeList")
    public ResponseEntity<?> noticeList(@RequestHeader("Authorization") String token) throws JsonProcessingException {

        return ResponseResult.result(crawlingService.getNoticeList(token));
    }

    @Operation(summary = "병 리스트 검색")
    @GetMapping("/sickList") //병 리스트 검색
    public ResponseEntity<?> ncpmcParser(@RequestBody RequestNcpmsSick request) {
        String urlBuilder = ncpmsManager.makeNcpmsSickSearchRequestUrl(request.getCropName(), request.getSickNameKor(), request.getDisplayCount(), request.getStartPoint());

        ServiceResult result;
        try {
            result = ncpmsService.returnResult(urlBuilder, true);
        } catch (Exception e) {
            NcpmsException exception = new NcpmsException(NcpmsError.NO_DATA_EXIST);
            result = ServiceResult.fail(String.valueOf(exception.getNcpmsError()), exception.getMessage());
        }
        return ResponseResult.result(result);
    }

    @Operation(summary = "병 상세정보 검색")
    @GetMapping("/sickDetail") //병 상세 검색
    public ResponseEntity<?> ncpmcParser2(@RequestBody RequestNcpmsSickDetail request) {
        String urlBuilder = ncpmsManager.makeNcpmsSickDetailSearchRequestUrl(request.getSickKey());

        ServiceResult result;
        try {
            result = ncpmsService.returnResult(urlBuilder, false);
        } catch (Exception e) {
            NcpmsException exception = new NcpmsException(NcpmsError.NO_DATA_EXIST);
            result = ServiceResult.fail(String.valueOf(exception.getNcpmsError()), exception.getMessage());
        }
        return ResponseResult.result(result);
    }
}
