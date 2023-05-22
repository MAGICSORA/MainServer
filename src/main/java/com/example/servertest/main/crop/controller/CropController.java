package com.example.servertest.main.crop.controller;

import com.example.servertest.main.crop.entity.SickList;
import com.example.servertest.main.crop.exception.NcpmsError;
import com.example.servertest.main.crop.exception.NcpmsException;
import com.example.servertest.main.crop.model.request.DiagnosisDto;
import com.example.servertest.main.crop.model.request.MapRequest;
import com.example.servertest.main.crop.model.request.SickListDto;
import com.example.servertest.main.crop.service.CategoryService;
import com.example.servertest.main.crop.service.CrawlingService;
import com.example.servertest.main.crop.service.MyCropHistoryService;
import com.example.servertest.main.crop.service.NaBatBuService;
import com.example.servertest.main.global.model.ResponseResult;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.api.ncpms.component.NcpmsManager;
import com.example.servertest.main.api.ncpms.model.request.RequestNcpmsSick;
import com.example.servertest.main.api.ncpms.model.request.RequestNcpmsSickDetail;
import com.example.servertest.main.api.ncpms.service.NcpmsService;
import com.example.servertest.main.api.psis.component.PsisManager;
import com.example.servertest.main.api.psis.model.request.RequestPsisInfo;
import com.example.servertest.main.api.psis.model.request.RequestPsisList;
import com.example.servertest.main.api.psis.service.PsisService;
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
public class CropController {

    private final NaBatBuService naBatBuService;
    private final PsisManager psisManager;
    private final PsisService psisService;
    private final NcpmsManager ncpmsManager;
    private final NcpmsService ncpmsService;
    private final TestService testService;
    private final CrawlingService crawlingService;
    private final CategoryService categoryService;
    private final MyCropHistoryService myCropHistoryService;

    @Operation(summary = "테스트-병해목록저장")
    @PostMapping("/input/sickList")
    public ResponseEntity<?> inputSickList(
            @RequestBody SickListDto sickListDto) {
        SickList sickList = naBatBuService.saveSickList(sickListDto);

        return ResponseEntity.ok(sickList);
    }

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

    @Operation(summary = "진단요청")
    @PostMapping("/diagnosis") //진단 요청
    public ResponseEntity<?> returnDiagnosis(
            @RequestPart(value = "requestInput") DiagnosisDto diagnosisDto
            , @RequestPart(value = "image") MultipartFile file, @RequestHeader("Authorization") String token) throws IOException {
        ServiceResult result = testService.returnDiagnosisResult(diagnosisDto, file, token);

        return ResponseResult.result(result);
    }

    @Operation(summary = "사용자 진단 기록 조회")
    @GetMapping("/diagnosisRecord") //사용자 진단 기록 조회
    public ResponseEntity<?> diagnosisRecord(@RequestParam Long diagnosisRecordId, @RequestHeader("Authorization") String token) throws JsonProcessingException {

        return ResponseResult.result(naBatBuService.getDiagnosisRecord(diagnosisRecordId, token));
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

    @Operation(hidden = true)
    @GetMapping("/formTest")
    public String test(@RequestPart(value = "id") String id, @RequestPart(value = "name") String name) {
        return id + "_" + name;
    }

    @Operation(summary = "반경 2km 병해 진단 기록 조회 -> 구현중")
    @GetMapping("/nearDisease") //반경 2km
    public ResponseEntity getNearDiseases(@RequestBody MapRequest mapRequest, @RequestHeader("Authorization") String token) {
        return ResponseResult.result(naBatBuService.getNearDiseases(mapRequest, token));
    }

    @Operation(summary = "카테고리 생성")
    @PostMapping("/category/create")
    public ResponseEntity<?> createCategory(@RequestParam String name, @RequestHeader("Authorization") String token) {
        return ResponseResult.result(categoryService.registerCategory(name, token));
    }

    @Operation(summary = "카테고리 목록 조회")
    @GetMapping("/category/list")
    public ResponseEntity<?> getCategoryList(@RequestHeader("Authorization") String token) {
        return ResponseResult.result(categoryService.getCategoryList(token));
    }

    @Operation(summary = "카테고리 변경")
    @PutMapping("/category/update")
    public ResponseEntity<?> updateCategory(@RequestParam String originalName, @RequestParam String changeName, @RequestParam String changeMemo, @RequestHeader("Authorization") String token) {

        return ResponseResult.result(categoryService.updateCategory(originalName, changeName, changeMemo, token));
    }

    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("/category/delete")
    public ResponseEntity<?> deleteCategory(@RequestHeader("Authorization") String token, @RequestParam Long categoryId) {

        return ResponseResult.result(categoryService.deleteCategory(token, categoryId));
    }

    @Operation(summary = "카테고리에 해당하는 진단 기록 조회")
    @GetMapping("/category/record")
    public ResponseEntity<?> getDiagnosisOfCategory(@RequestHeader("Authorization") String token, @RequestParam Long categoryId) {

        return ResponseResult.result(categoryService.getDiagnosisOfCategory(token, categoryId));
    }

    @Operation(summary = "진단 기록의 카테고리 변경")
    @PostMapping("/diagnosisRecord/{recordId}/category/update")
    public ResponseEntity<?> updateRecordCategory(@RequestHeader("Authorization") String token, @PathVariable Long recordId, @RequestParam Long categoryId) {
        return ResponseResult.result(categoryService.updateRecordCategory(token, recordId, categoryId));
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

    @Operation(summary = "작물 관리 일지 추가")
    @PostMapping("/manage/create")
    public ResponseEntity<?> createManage(@RequestHeader("Authorization") String token, @RequestParam Long diagnosisId, @RequestParam String contents) {

        return ResponseResult.result(myCropHistoryService.registerContents(token, diagnosisId, contents));
    }

    @Operation(summary = "작물 관리 일지 리스트 조회")
    @GetMapping("/manage/read/list")
    public ResponseEntity<?> listManage(@RequestHeader("Authorization") String token, @RequestParam Long diagnosisRecordId) {

        return ResponseResult.result(myCropHistoryService.contentsList(token, diagnosisRecordId));
    }

    @Operation(summary = "작물 관리 일지 상세 조회")
    @GetMapping("/manage/read/detail")
    public ResponseEntity<?> readManage(@RequestHeader("Authorization") String token, @RequestParam Long myCropId) {

        return ResponseResult.result(myCropHistoryService.contentsDetail(token, myCropId));
    }

    @Operation(summary = "작물 관리 일지 변경")
    @PutMapping("/manage/update")
    public ResponseEntity<?> updateManage(@RequestHeader("Authorization") String token, @RequestParam Long myCropId, @RequestParam String contents) {

        return ResponseResult.result(myCropHistoryService.updateMemo(token, myCropId, contents));
    }

    @Operation(summary = "작물 관리 일지 삭제")
    @DeleteMapping("/manage/delete")
    public ResponseEntity<?> deleteManage(@RequestHeader("Authorization") String token, @RequestParam Long myCropId) {

        return ResponseResult.result(myCropHistoryService.deleteMemo(token, myCropId));
    }
}