package com.example.servertest.controller;

import com.example.servertest.service.TestService;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(value = "/test/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getSickDetail(@RequestParam String cropType,
        @RequestParam String sickType, @RequestParam String name) throws IOException {

        InputStream imageStream = new FileInputStream(
            "src\\main\\resources\\images\\"
                + cropType + "\\" + sickType + "\\" + name);

        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
    }
}
