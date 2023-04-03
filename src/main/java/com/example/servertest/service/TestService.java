package com.example.servertest.service;

import com.example.servertest.entity.TestEntity;
import com.example.servertest.repository.TestRepository;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {

    List<String> files = new ArrayList<>();
    //    private final String rootLocation = "home\\ubuntu\\serverTest2\\src\\main\\resources\\images";
    private final String rootLocation = "C:\\dev\\workspace\\serverTest\\src\\main\\resources\\images";

    private final TestRepository testRepository;

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

    public ResponseEntity<?> getSickDetail(String cropType, String sickType) {

        Path directoryPath = Paths.get(
            rootLocation + "\\" + cropType + "\\" + sickType + "\\"
                + "사이즈조정4.jpg");

        System.out.println(directoryPath);

        BufferedImage image;
        File imageFile;
        imageFile = new File(directoryPath.toString());
//            image = ImageIO.read(imageFile);

        return ResponseEntity.status(HttpStatus.OK).body(imageFile);
    }
}
