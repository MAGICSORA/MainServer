package com.example.servertest.main.crop.controller;

import com.example.servertest.main.crop.entity.SickList;
import com.example.servertest.main.crop.exception.NcpmsError;
import com.example.servertest.main.crop.exception.NcpmsException;
import com.example.servertest.main.crop.model.request.DiagnosisDto;
import com.example.servertest.main.crop.model.request.SickListDto;
import com.example.servertest.main.crop.model.response.DiagnosisResponse;
import com.example.servertest.main.crop.service.CategoryService;
import com.example.servertest.main.crop.service.CrawlingService;
import com.example.servertest.main.crop.service.NaBatBuService;
import com.example.servertest.main.global.model.ResponseResult;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.ncpms.component.NcpmsManager;
import com.example.servertest.main.ncpms.model.request.RequestNcpmsSick;
import com.example.servertest.main.ncpms.model.request.RequestNcpmsSickDetail;
import com.example.servertest.main.ncpms.service.NcpmsService;
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
    private final NcpmsManager ncpmsManager;
    private final NcpmsService ncpmsService;
    private final CrawlingService crawlingService;
    private final CategoryService categoryService;

    @PostMapping("/input/sickList")
    public ResponseEntity<?> inputSickList(
            @RequestBody SickListDto sickListDto) {
        SickList sickList = naBatBuService.saveSickList(sickListDto);

        return ResponseEntity.ok(sickList);
    }

    @GetMapping("/psisList") //농약 리스트 조회
    public Map<String, ?> krxParser2(@RequestBody RequestPsisList request) throws IOException {
        String urlBuilder = psisManager.makePsisListRequestUrl(request.getCropName(), request.getDiseaseWeedName(), request.getDisplayCount(), request.getStartPoint());

        System.out.println(urlBuilder);
        return psisService.returnResult(urlBuilder, true);
    }

    @GetMapping("/psisDetail") //농약 상세정보 조회
    public Map<String, ?> krxParser3(@RequestBody RequestPsisInfo request) throws IOException {
        String urlBuilder = psisManager.makePsisInfoRequestUrl(request.getPestiCode(), request.getDiseaseUseSeq(), request.getDisplayCount(), request.getStartPoint());

        System.out.println(urlBuilder);
        return psisService.returnResult(urlBuilder, false);
    }

    @PostMapping("/diagnosis") //진단 요청
    public ResponseEntity<?> returnDiagnosis(
            @RequestPart(value = "requestInput") DiagnosisDto diagnosisDto
            , @RequestPart(value = "image") MultipartFile file, @RequestHeader("Authorization") String token) throws IOException {
        ServiceResult result = naBatBuService.returnDiagnosisResult(diagnosisDto, file, token);

        return ResponseResult.result(result);
    }

    @GetMapping("/diagnosisRecord") //사용자 진단 기록 조회
    public ResponseEntity<?> diagnosisRecord(@RequestParam Long diagnosisRecordId) throws JsonProcessingException {

        return ResponseResult.result(naBatBuService.getDiagnosisRecord(diagnosisRecordId));
    }

    @PutMapping("/save/noticeList")//메인페이지 병해발생정보 리스트 저장
    public void saveNoticeList() {

        String idx = crawlingService.getUrl(crawlingService.getIndex());

        crawlingService.updateAllData(idx);

//        System.out.println(crawlingService.getAllData(idx));
    }

    @GetMapping("/noticeList")
    public ResponseEntity<?> noticeList(@RequestHeader("Authorization") String token) throws JsonProcessingException {

        return ResponseResult.result(crawlingService.getNoticeList(token));
    }

    @GetMapping("/formTest")
    public String test(@RequestPart(value = "id") String id, @RequestPart(value = "name") String name) {
        return id + "_" + name;
    }

    @GetMapping("/nearDisease") //반경 1km
    public ResponseEntity getNearDiseases(@RequestParam float latitude, @RequestParam float longitude, @RequestHeader("Authorization") String token) {
        return ResponseResult.result(naBatBuService.getNearDiseases(latitude, longitude, token));
    }

    @PostMapping("/category/create")
    public ResponseEntity<?> createCategory(@RequestParam String name, @RequestHeader("Authorization") String token) {
        return ResponseResult.result(categoryService.registerCategory(name, token));
    }

    @GetMapping("/category/list")
    public ResponseEntity<?> getCategoryList(@RequestHeader("Authorization") String token) {
        return ResponseResult.result(categoryService.getCategoryList(token));
    }

    @PutMapping("/category/update")
    public ResponseEntity<?> updateCategory(@RequestParam String originalName, @RequestParam String changeName, @RequestHeader("Authorization") String token) {

        return ResponseResult.result(categoryService.updateCategory(originalName, changeName, token));
    }

    @DeleteMapping("/category/delete")
    public ResponseEntity<?> deleteCategory(@RequestHeader("Authorization") String token, @RequestParam Long categoryId) {

        return ResponseResult.result(categoryService.deleteCategory(token, categoryId));
    }

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