package com.example.servertest.controller;

import com.example.servertest.service.TestService;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/save")
    public void testDb(@RequestParam String input) {

        testService.test(input);
    }

    @GetMapping("/hi")
    public String test() {
        return "hi";
    }

    @GetMapping("/zz")
    public String sec() {
        return "남이경 호구";
    }

    @GetMapping("/showall")
    public List showAll() {
        return testService.showAll();
    }

    @GetMapping("/delete")
    public void delete(@RequestParam String input) {
        testService.deleteTest(input);
    }
}
