package com.example.servertest.main.psis.controller;

import com.example.servertest.main.psis.component.PsisManager;
import com.example.servertest.main.psis.model.request.RequestPsisInfo;
import com.example.servertest.main.psis.model.request.RequestPsisList;
import com.example.servertest.main.psis.service.PsisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PsisController {

    private final PsisManager psisManager;
    private final PsisService psisService;

    @GetMapping("/psisList")
    public Map<String, ?> krxParser2(@RequestBody RequestPsisList request) throws IOException {
        String urlBuilder = psisManager.makePsisListRequestUrl(request.getCropName(), request.getDiseaseWeedName(), request.getDisplayCount(), request.getStartPoint());

        return psisService.returnResult(urlBuilder, true);
    }

    @GetMapping("/psisInfo")
    public Map<String, ?> krxParser3(@RequestBody RequestPsisInfo request) throws IOException {
        String urlBuilder = psisManager.makePsisInfoRequestUrl(request.getPestiCode(), request.getDiseaseUseSeq(), request.getDisplayCount(), request.getStartPoint());

        return psisService.returnResult(urlBuilder, false);
    }
}
