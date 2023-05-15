package com.example.servertest.main.test.controller;

import com.example.servertest.main.crop.entity.DiseaseDetail;
import com.example.servertest.main.crop.repository.DiseaseDetailRepository;
import com.example.servertest.main.test.model.InputDiseaseDetail;
import com.example.servertest.main.test.service.TestService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import com.example.servertest.main.crop.type.CropType;
import com.example.servertest.main.crop.type.DiseaseCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TestService testService;
    private final DiseaseDetailRepository diseaseDetailRepository;

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

    @PutMapping("/input/diseaseDetail")
    public ResponseEntity<?> test55(@RequestBody InputDiseaseDetail inputDiseaseDetail) {

        Optional<DiseaseDetail> optionalDiseaseDetail = diseaseDetailRepository.findById(inputDiseaseDetail.getId());
        DiseaseDetail diseaseDetail = optionalDiseaseDetail.get();
        diseaseDetail.setEnvironment(inputDiseaseDetail.getEnvironment());
        diseaseDetail.setControlMethod(inputDiseaseDetail.getControlMethod());
        diseaseDetail.setSymptom(inputDiseaseDetail.getSymptom());
        diseaseDetailRepository.save(diseaseDetail);

        return ResponseEntity.ok(diseaseDetail);
    }

    @GetMapping("/flask")
    public ResponseEntity<?> test66() {
        HttpHeaders header = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(header);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://15.164.23.13:8080/test/hi"
                , HttpMethod.GET, entity, String.class);

        return responseEntity;
    }

    @GetMapping("/test/flask")
    public ResponseEntity<?> test77() {
        HttpHeaders header = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(header);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://3.35.146.68:5000/predict"
                , HttpMethod.GET, entity, String.class);

        return responseEntity;
    }
}
