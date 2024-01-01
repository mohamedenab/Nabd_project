package com.example.nabd.controller;

import com.example.nabd.service.UploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin()
@RequestMapping("/api/v1/upload")
public class UploadController {
    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_SU')")
    public ResponseEntity<Object> uploadData(@RequestParam("file") MultipartFile file){
        return uploadService.uploadDataFromExcelFile(file);
    }

}
