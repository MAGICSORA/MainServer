package com.example.servertest.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileController {

    private final String rootLocation = "../../src/main/resources/images/";

    @PostMapping("/savefile") //사용자 이미지 저장
    public ResponseEntity<?> handleFileUpload(
        @RequestParam("file") MultipartFile file, @RequestParam String userName,
        @RequestParam String saveName)
        throws IOException {

        BufferedImage bi = ImageIO.read(file.getInputStream());
        bi = simpleResizeImage(bi, 614, 614);

        Path directoryPath = Paths.get(rootLocation + "User/" + userName);

        try {
            Files.createDirectory(directoryPath);
        } catch (FileAlreadyExistsException e) {

        } finally {
            String url = directoryPath + "/" + saveName + ".jpg";
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
}
