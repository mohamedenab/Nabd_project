package com.example.nabd.service;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.medicineDtos.MedicineDto;

import org.springframework.http.ResponseEntity;


public interface IMedicineService {
    BasisResponse create(MedicineDto medicineDto);
    BasisResponse update(Long id, MedicineDto medicineDto);

    BasisResponse getMedicine(int pageNo, int pageSize, String sortBy, String filter);

    BasisResponse getMedicineNameById(Long id);

    BasisResponse getPatientMedicine(Long id);

    BasisResponse replaceMedicineWithAnother(Long firstId, Long secondId);

    BasisResponse removeMedicineFromPatient(Long medicineId, Long patentId);

    ResponseEntity<Object> delete(Long name);

    BasisResponse deactivatemedicine(Long medicineId, Long patientId);
    BasisResponse activatemedicine(Long medicineId, Long patientId);

}
