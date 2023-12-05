package com.example.nabd.service;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.MedicineDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IMedicineService {
    BasisResponse create(MedicineDto medicineDto);

    BasisResponse getMedicine(int pageNo, int pageSize, String sortBy, String filter);

    BasisResponse getMedicineNameById(Long id);

    BasisResponse getPatientMedicine(Long id);

    BasisResponse replaceMedicineWithAnother(Long firstId, Long secondId);

    String removeMedicineFromPatient(Long medicineId, Long patentId);

    ResponseEntity<Object> delete(Long name);
}
