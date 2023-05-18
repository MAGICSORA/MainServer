package com.example.servertest.main.crop.service;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileService {

    private final String rootLocation = "../../src/main/resources/images/";

    BufferedImage simpleResizeImage(BufferedImage originalImage,
                                    int targetWidth, int targetHeight) {
        return Scalr.resize(originalImage, targetWidth, targetHeight);
    }

    public BufferedImage handleFileUpload(
            MultipartFile file, String userName, String imgCode)
            throws IOException {

        InputStream stream = file.getInputStream();
        BufferedImage bi = ImageIO.read(stream);
        bi = simpleResizeImage(bi, 640, 640);

        Path directoryPath = Paths.get(rootLocation + "User/" + userName);

        try {
            Files.createDirectory(directoryPath);
        } catch (FileAlreadyExistsException e) {

        } finally {
            String url = directoryPath + "/" + imgCode + ".jpg";
            ImageIO.write(bi, "jpg", new File(url));
            stream.close();
        }

        return bi;
    }
}
