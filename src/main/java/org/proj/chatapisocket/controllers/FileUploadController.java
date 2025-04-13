package org.proj.chatapisocket.controllers;

import org.proj.chatapisocket.services.MinIOService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
        String[] mass = fileUrl.split("/");
        return ResponseEntity.ok(mass[mass.length-1]);
    }
//    @GetMapping("/{filename:.+}")
//    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
//        Resource resource = minioService.getFile(filename);
//        String contentType = minioService.getContentType(filename);
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
//                .body(resource);
//    }
    @GetMapping("/{filename:.+}")
    public ResponseEntity<?> getFileUrl(@PathVariable String filename){
        try {
            return ResponseEntity.ok(minioService.generatePresignedUrl(filename));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}

