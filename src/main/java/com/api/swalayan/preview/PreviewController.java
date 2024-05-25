package com.api.swalayan.preview;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("preview")
public class PreviewController {
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> previewImage(@PathVariable("filename") String filename) throws IOException {
        Path filePath = Paths.get("/home/zayndev/uploads", filename).toAbsolutePath();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE) // Sesuaikan dengan tipe gambar
                    .body(resource);
        } else {
            throw new FileNotFoundException("File not found");
        }
    }
}
