package com.example.servertest.main.test.controller;

import com.example.servertest.main.nabatbu.cropInfo.entity.DiseaseDetail;
import com.example.servertest.main.nabatbu.cropInfo.ncpms.component.NcpmsManager;
import com.example.servertest.main.nabatbu.cropInfo.ncpms.model.response.NcpmsSickService;
import com.example.servertest.main.nabatbu.cropInfo.ncpms.service.NcpmsService;
import com.example.servertest.main.nabatbu.cropInfo.service.CrawlingService;
import com.example.servertest.main.nabatbu.diagnosis.model.request.DiagnosisDto;
import com.example.servertest.main.nabatbu.diagnosis.repository.DiseaseDetailRepository;
import com.example.servertest.main.global.type.CropType;
import com.example.servertest.main.global.type.DiseaseCode;
import com.example.servertest.main.global.model.ResponseResult;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.test.model.InputDiseaseDetail;
import com.example.servertest.main.test.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TestService testService;
    private final CrawlingService crawlingService;
    private final NcpmsService ncpmsService;
    private final NcpmsManager ncpmsManager;

    private final DiseaseDetailRepository diseaseDetailRepository;

    @GetMapping("/save")
//    @Operation(summary = "템플릿 화면", description = "템플릿 화면을 출력합니다.", tags = {"View"})
    public void testDb(@RequestParam String input) {

        testService.test(input);
    }

    @GetMapping("/hi")
    public String test() {
        return "hi";
    }

    @Operation(hidden = true)
    @GetMapping("/gift/NamLK")
    public String fun() {
        return "속았지 ㅋ";
    }

    @Operation(hidden = true)
    @GetMapping(value = "/gift/NamLK/22", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> test22() throws IOException {

        InputStream imageStream = new FileInputStream(
                "../../src/main/resources/images/" + "User/" + "사용자5" + "/" + "샘플.png");

        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }

    @GetMapping("/showall")
    public List showAll() {
        return testService.showAll();
    }

    @GetMapping("/delete")
    public void delete(@RequestParam String input) {
        testService.deleteTest(input);
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

    @PostMapping("/diagnosis/flask")
    @Operation(summary = "더미데이터로 병해진단 테스트")
    public ResponseEntity<?> test77(
            @RequestPart(value = "requestInput") DiagnosisDto diagnosisDto
            , @RequestPart(value = "image") MultipartFile file, @RequestHeader("Authorization") String token) throws IOException {

        ServiceResult result = testService.returnDiagnosisResult(diagnosisDto, file, token);

        return ResponseResult.result(result);
    }

    @Operation(hidden = true)
    @PostMapping("/save/codes")
    public ResponseEntity<?> test88(@RequestParam int start, @RequestParam int count) throws IOException, InterruptedException {
//        ServiceResult result = testService.save(start, count);
        return null;
    }

    @PostMapping("/save/codes/2")
    @Operation(summary = "NCPMS API호출을 위한 sickKey 저장")
    public ResponseEntity<?> test99(@RequestParam int idx, @RequestParam int count) throws IOException, InterruptedException {
//        String url = crawlingService.getUrl2(idx);
        String url;
        for (int i = 0; i < count; i++) {
            url = crawlingService.getUrl2(idx + i);
            crawlingService.save2(url);
        }
        return null;
    }

    @GetMapping("/save/codes/3")
    @Operation(summary = "병해 썸이미지 반환")
    public ResponseEntity<?> test99(@RequestParam String cropName, @RequestParam String sickNameKor) throws IOException, InterruptedException {

        String url = ncpmsManager.makeNcpmsSickSearchRequestUrl(cropName, sickNameKor, "1", "1");

//        Map<String, NcpmsSickService> result = new HashMap<>();
        Map<String, NcpmsSickService> result = (Map<String, NcpmsSickService>) ncpmsService.returnResult(url, true).getObject();
        NcpmsSickService ncpmsSickService = result.get("response");
        System.out.println(ncpmsSickService.getList().getItem().get(0).getThumbImg());

        return null;
    }
}
