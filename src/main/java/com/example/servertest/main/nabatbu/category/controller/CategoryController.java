package com.example.servertest.main.nabatbu.category.controller;

import com.example.servertest.main.nabatbu.cropInfo.ncpms.component.NcpmsManager;
import com.example.servertest.main.nabatbu.cropInfo.ncpms.service.NcpmsService;
import com.example.servertest.main.nabatbu.cropInfo.psis.component.PsisManager;
import com.example.servertest.main.nabatbu.cropInfo.psis.service.PsisService;
import com.example.servertest.main.nabatbu.category.service.CategoryService;
import com.example.servertest.main.nabatbu.category.service.MyCropHistoryService;
import com.example.servertest.main.nabatbu.cropInfo.service.CrawlingService;
import com.example.servertest.main.global.model.ResponseResult;
import com.example.servertest.main.test.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crop")
public class CategoryController {

    private final PsisManager psisManager;
    private final PsisService psisService;
    private final NcpmsManager ncpmsManager;
    private final NcpmsService ncpmsService;
    private final TestService testService;
    private final CrawlingService crawlingService;
    private final CategoryService categoryService;
    private final MyCropHistoryService myCropHistoryService;

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

    @Operation(summary = "카테고리 단일 조회")
    @GetMapping("/category/name")
    public ResponseEntity<?> getCategoryList(@RequestHeader("Authorization") String token, @RequestParam String name) {
        return ResponseResult.result(categoryService.getOneCategory(token, name));
    }

    @Operation(summary = "카테고리 변경")
    @PutMapping("/category/update")
    public ResponseEntity<?> updateCategory(@RequestParam Long id, @RequestParam String changeName, @RequestParam String changeMemo, @RequestHeader("Authorization") String token) {

        return ResponseResult.result(categoryService.updateCategory(id, changeName, changeMemo, token));
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
