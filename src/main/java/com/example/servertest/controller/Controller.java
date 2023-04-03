package com.example.servertest.controller;

import com.example.servertest.model.SickService;
import com.example.servertest.service.NaBatBuService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final NaBatBuService naBatBuService;

    @GetMapping("/find/sickList")
    public ResponseEntity<?> sickList(String cropName, String sickNameKor)
        throws IOException {

        SickService service = naBatBuService.getSickList(cropName, sickNameKor);

        return ResponseEntity.ok(service);
    }
}
