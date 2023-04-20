package com.example.servertest.main.test.controller;

import com.example.servertest.main.test.service.TestService;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.example.servertest.main.crop.type.CropType;
import com.example.servertest.main.crop.type.DiseaseCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
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

    @GetMapping("/gift/NamLK")
    public String fun() {
        return "속았지 ㅋ";
    }

    @GetMapping("/showall")
    public List showAll() {
        return testService.showAll();
    }

    @GetMapping("/delete")
    public void delete(@RequestParam String input) {
        testService.deleteTest(input);
    }

    @GetMapping(value = "/gift/NamLK/22", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> test22() throws IOException {

        InputStream imageStream = new FileInputStream(
                "../../src/main/resources/images/" + "User/" + "사용자5" + "/" + "샘플.png");

        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }

    @GetMapping("/getCropCode/{index}")
    public ResponseEntity<?> test33(@PathVariable int index) {

        CropType value = CropType.values()[index];
        return ResponseEntity.ok(value);
    }

    @GetMapping("/getDiseaseCode/{index}")
    public ResponseEntity<?> test44(@PathVariable int index) {

        DiseaseCode value = DiseaseCode.values()[index];
        return ResponseEntity.ok(value);
    }
}
