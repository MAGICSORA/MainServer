package com.example.servertest.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploader {

    List<String> files = new ArrayList<>();
    private final Path rootLocation = Paths.get(
        "C:\\dev\\workspace\\serverTest\\src\\main\\resources\\images");

    @PostMapping("/savefile")
    public ResponseEntity<?> handleFileUpload(
        @RequestParam("file") MultipartFile file, @RequestParam String userName,
        @RequestParam String saveName)
        throws IOException {
        String message;
//        try {
//            try {
//                Files.copy(file.getInputStream(),
//                    this.rootLocation.resolve(saveName+".jpg"));
//            } catch (Exception e) {
//                throw new RuntimeException("FAIL!");
//            }
//            files.add(file.getOriginalFilename());
//
//            message = "Successfully uploaded!";
//            return ResponseEntity.status(HttpStatus.OK).body(message);
//        } catch (Exception e) {
//            message = "Failed to upload!";
//            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
//                .body(message);
//        }

        BufferedImage bi = ImageIO.read(file.getInputStream());
        bi = simpleResizeImage(bi, 614, 614);
        String url = rootLocation + "\\" + userName + saveName + ".jpg";
        ImageIO.write(bi, "jpg", new File(url));

        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    BufferedImage simpleResizeImage(BufferedImage originalImage,
        int targetWidth, int targetHeight) {
        return Scalr.resize(originalImage, targetWidth, targetHeight);
    }
//    public String uploadFile(String originImgName,MultipartFile file,String postImgLocation) throws Exception{
//        UUID uuid=UUID.randomUUID();
//        String extension=originImgName.substring(originImgName.lastIndexOf("."));
//        String savedFileName=uuid.toString()+extension;
//        String fileUploadUrl=postImgLocation+savedFileName;
////----------------------------------------------------------------
//        //mutipartfile->bufferedImage
//        BufferedImage bi=ImageIO.read(file.getInputStream());
//        //이미지 사이즈변경
//        bi=resizeImage(bi,450,450);
//        //이미지 위치에 저장
//        ImageIO.write(bi,"jpg",new File(fileUploadUrl));
////-------------------------------------------------------------------
//        return savedFileName;
//
//    }
}
