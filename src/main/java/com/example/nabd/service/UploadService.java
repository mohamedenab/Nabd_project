package com.example.nabd.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String uploadDataFromExcelFile(MultipartFile file);

}
