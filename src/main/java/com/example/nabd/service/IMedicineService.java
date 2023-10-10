package com.example.nabd.service;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.MedicineDto;
import org.springframework.web.multipart.MultipartFile;

public interface IMedicineService {
    BasisResponse create(MedicineDto medicineDto);
    BasisResponse getMedicine(int pageNo, int pageSize, String sortBy,  String filter);
    BasisResponse replaceMedicineWithAnother(Long firstId , Long secondId );
    String delete(String name);
}
