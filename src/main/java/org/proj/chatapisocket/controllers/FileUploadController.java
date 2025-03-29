package org.proj.chatapisocket.controllers;

import org.proj.chatapisocket.services.MinIOService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {
    private final MinIOService minioService;

    public FileUploadController(MinIOService minioService) {
        this.minioService = minioService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = minioService.uploadFile(file);
        return ResponseEntity.ok(fileUrl);
    }
}

