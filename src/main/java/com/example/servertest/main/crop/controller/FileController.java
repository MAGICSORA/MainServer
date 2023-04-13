package com.example.servertest.main.crop.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileController {

    private final String rootLocation = "../../src/main/resources/images/";

    @PostMapping("/savefile") //사용자 이미지 저장
    public ResponseEntity<?> handleFileUpload(
            @RequestParam("file") MultipartFile file, @RequestParam String userName)
            throws IOException {

        BufferedImage bi = ImageIO.read(file.getInputStream());
        bi = simpleResizeImage(bi, 640, 640);

        Path directoryPath = Paths.get(rootLocation + "User/" + userName);
        String dateTime = LocalDateTime.now().toString();
        dateTime = dateTime.replace(":", "");

        try {
            Files.createDirectory(directoryPath);
        } catch (FileAlreadyExistsException e) {

        } finally {
            String url = directoryPath + "/" + userName + dateTime + ".jpg";
            ImageIO.write(bi, "jpg", new File(url));
        }

        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    BufferedImage simpleResizeImage(BufferedImage originalImage,
                                    int targetWidth, int targetHeight) {
        return Scalr.resize(originalImage, targetWidth, targetHeight);
    }

    //저장된 이미지 추출
    @GetMapping(value = "/test/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getSickDetail(@RequestParam String cropType,
                                           @RequestParam String sickType, @RequestParam String name)
            throws IOException {

        InputStream imageStream = new FileInputStream(
                rootLocation + "AIHub/" + cropType + "/" + sickType + "/" + name);

        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
    }

    @GetMapping(value = "/image/{userName}/{imageCode}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getSickDetail(@PathVariable String userName, @PathVariable String imageCode)
            throws IOException {

        InputStream imageStream = new FileInputStream(
                rootLocation + "User/" + userName + "/" + imageCode + ".jpg");

        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
    }
}
