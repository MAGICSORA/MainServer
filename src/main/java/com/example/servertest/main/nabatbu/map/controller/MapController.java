package com.example.servertest.main.nabatbu.map.controller;

import com.example.servertest.main.nabatbu.map.model.request.MapRequest;
import com.example.servertest.main.nabatbu.map.service.MapService;
import com.example.servertest.main.global.model.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crop")
public class MapController {

    private final MapService mapService;

    @Operation(summary = "반경 2km 병해 진단 기록 조회 -> 구현중")
    @PostMapping("/nearDisease") //반경 2km
    public ResponseEntity getNearDiseases(@RequestBody MapRequest mapRequest, @RequestHeader("Authorization") String token) {
        return ResponseResult.result(mapService.getNearDiseases(mapRequest, token));
    }
}
