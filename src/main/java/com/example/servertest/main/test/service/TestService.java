package com.example.servertest.main.test.service;

import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.nabatbu.category.entity.Category;
import com.example.servertest.main.nabatbu.category.repository.CategoryRepository;
import com.example.servertest.main.nabatbu.cropInfo.entity.SickList;
import com.example.servertest.main.nabatbu.cropInfo.ncpms.component.NcpmsManager;
import com.example.servertest.main.nabatbu.cropInfo.ncpms.model.response.NcpmsSickDetailService;
import com.example.servertest.main.nabatbu.cropInfo.ncpms.service.NcpmsService;
import com.example.servertest.main.nabatbu.cropInfo.repository.SickListRepository;
import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisRecord;
import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisResult;
import com.example.servertest.main.nabatbu.diagnosis.model.request.DiagnosisDto;
import com.example.servertest.main.nabatbu.diagnosis.model.response.DiagnosisOutput;
import com.example.servertest.main.nabatbu.diagnosis.model.response.DiagnosisResponse;
import com.example.servertest.main.nabatbu.diagnosis.repository.DiagnosisRecordRepository;
import com.example.servertest.main.nabatbu.diagnosis.repository.DiagnosisResultRepository;
import com.example.servertest.main.nabatbu.diagnosis.service.FileService;
import com.example.servertest.main.nabatbu.member.entity.Member;
import com.example.servertest.main.nabatbu.member.exception.MemberError;
import com.example.servertest.main.nabatbu.member.service.MemberService;
import com.example.servertest.main.test.entity.TestEntity;
import com.example.servertest.main.test.repository.TestRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final MemberService memberService;
    private final DiagnosisRecordRepository diagnosisRecordRepository;
    private final FileService fileService;
    private final CategoryRepository categoryRepository;
    private final DiagnosisResultRepository diagnosisResultRepository;
    private final NcpmsService ncpmsService;
    private final NcpmsManager ncpmsManager;
    private final SickListRepository sickListRepository;

    public void test(String input) {
        testRepository.save(TestEntity.builder().content(input).build());
    }

    public List showAll() {
        List<TestEntity> testEntityList = testRepository.findAll();
        List list = new ArrayList();
        for (TestEntity test : testEntityList) {
            list.add(test.getContent());
        }
        return list;
    }

    public void deleteTest(String input) {
        TestEntity test = testRepository.findByContent(input);
        testRepository.delete(test);
    }

    public ServiceResult returnDiagnosisResult(DiagnosisDto diagnosisDto, MultipartFile file, String token) throws IOException {

        Member member = new Member();
        try {
            member = memberService.validateMember(token);
        } catch (ExpiredJwtException e) {
            MemberError error = MemberError.EXPIRED_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
//            e.printStackTrace();
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        StringBuilder imgCode = new StringBuilder();
        imgCode.append(member.getId());
        imgCode.append("-");

        Long cnt;
        if (diagnosisRecordRepository.count() != 0) {
            cnt = diagnosisRecordRepository.findTopByOrderByIdDesc().getId() + 1;
        } else {
            cnt = 1L;
        }

        imgCode.append(cnt); //저장 이미지 파일 명 설정

//        Optional<Member> optionalMember = memberRepository.findById(diagnosisDto.getUserId());
//        Member member = optionalMember.get(); //이미지 경로를 위한 member

        BufferedImage image = fileService.handleFileUpload(file, member.getName(), String.valueOf(imgCode));//이미지 저장

        /*
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://3.35.146.68:5000/predict";

        HttpHeaders header = new HttpHeaders();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("cropImage", file);
        body.add("cropType", diagnosisDto.getCropType());
        body.add("cropImageId", imgCode);
//        String requestBody = "{}";

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, header);

        ResponseEntity<AIResponse> responseEntity = restTemplate.exchange(url
                , HttpMethod.POST, entity, AIResponse.class);

        // 응답 처리
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            AIResponse responseBody = responseEntity.getBody();
            // 응답 처리 로직 작성
        } else {
            // 에러 처리 로직 작성
        }

        AIResponse aiResponse = responseEntity.getBody();
        */

        StringBuilder imagePath = new StringBuilder();
        imagePath.append("http://15.164.23.13:8080/image/");
//        imagePath.append("http://localhost:8080/image/");
        imagePath.append(member.getEmail());
        imagePath.append("/");
        imagePath.append(imgCode);

        Category category = categoryRepository.findByNameAndUserId("unclassified", member.getId());

        if (category == null) {
            Exception e = new Exception("실패");
            return ServiceResult.fail(e.getMessage(), e.getMessage());
        }

        DiagnosisRecord diagnosisRecord = DiagnosisRecord.builder()
                .userId(member.getId())
                .userLatitude(diagnosisDto.getUserLatitude())
                .userLongitude(diagnosisDto.getUserLongitude())
                .regDate(LocalDateTime.now())
                .cropType(diagnosisDto.getCropType())
                .imagePath(imagePath.toString())
                .categoryId(category.getId())
                .build();

        diagnosisRecordRepository.save(diagnosisRecord);

        DiagnosisResult diagnosisResult1 = DiagnosisResult.builder()
                .responseCode(1)
                .diagnosisRecord(diagnosisRecord)
                .diseaseCode(1)
                .accuracy(0.89F)
                .boxX1(0.01F)
                .boxX2(0.01F)
                .boxY1(0.02F)
                .boxY2(0.02F)
                .build();

        DiagnosisResult diagnosisResult2 = DiagnosisResult.builder()
                .responseCode(1)
                .diagnosisRecord(diagnosisRecord)
                .diseaseCode(1)
                .accuracy(0.89F)
                .boxX1(0.01F)
                .boxX2(0.01F)
                .boxY1(0.02F)
                .boxY2(0.02F)
                .build();


        DiagnosisResponse diagnosisResponse = DiagnosisResponse.builder()
                .diagnosisRecordId(diagnosisRecord.getId())
                .responseCode(1)
                .cropType(diagnosisDto.getCropType())
                .regDate(diagnosisDto.getRegDate())
                .diagnosisResults(List.of(DiagnosisOutput.to(diagnosisResult1), DiagnosisOutput.to(diagnosisResult2)))
                .imagePath(imagePath.toString())
                .build();

        diagnosisResultRepository.save(diagnosisResult1);
        diagnosisResultRepository.save(diagnosisResult2);
//        diagnosisResultRepository.save(diagnosisResult3);

        return ServiceResult.success(diagnosisResponse);
    }

    public ServiceResult save(String sickKey) throws IOException, InterruptedException {

        System.out.println(sickKey);
        String urlBuilder = ncpmsManager.makeNcpmsSickDetailSearchRequestUrl(sickKey);
        URL url = new URL(urlBuilder);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        // String 형식의 xml
        String xml = sb.toString();

        Map<String, NcpmsSickDetailService> result = new HashMap<>();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(NcpmsSickDetailService.class); // JAXB Context 생성
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();  // Unmarshaller Object 생성
            NcpmsSickDetailService apiResponse = (NcpmsSickDetailService) unmarshaller.unmarshal(new StringReader(xml)); // unmarshall 메서드 호출
            result.put("response", apiResponse);
            String cropName = apiResponse.getCropName();
            String sickNameKor = apiResponse.getSickNameKor();
            String sickNameEng = apiResponse.getSickNameEng();
//                String sickNameEng = apiResponse.getSickNameEng();
            if (cropName == null || sickNameEng == null || sickNameKor == null) {
                return null;
            }
            System.out.println("cropName: " + cropName + "/ sickNameKor: " + sickNameKor);
            sickListRepository.save(SickList.builder().sickKey(sickKey).cropName(cropName).sickNameEng(sickNameEng).sickNameKor(sickNameKor).build());

        } catch (JAXBException e) {
            e.printStackTrace();
        }

//    }
//        return null;
//}
        return null;
    }
}
