package com.api.swalayan.util;

import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;

public class UploadFile {
    @SneakyThrows
    public static String Upload(MultipartFile foto,String sub) {

        if (foto == null || foto.isEmpty()) {
            return null; //
        }

        // Create directory if it doesn't exist
        String UPLOAD_FOLDER = "./uploads/"+sub;
        File uploadDir = new File(UPLOAD_FOLDER);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        // Save the file to the specified folder
        LocalDateTime currentDateTime = LocalDateTime.now();
        String extension = Objects.requireNonNull(foto.getContentType()).split("/")[1];
        String fileName=currentDateTime+"."+extension;
        Path path = Paths.get(UPLOAD_FOLDER + File.separator + fileName);
        Files.copy(foto.getInputStream(), path);
        return fileName;
    }
}
