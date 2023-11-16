package com.example.nabd.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ResponseEntity<Object> uploadDataFromExcelFile(MultipartFile file);

}
