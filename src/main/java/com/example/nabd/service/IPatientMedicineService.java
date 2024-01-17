package com.example.nabd.service;

import com.example.nabd.dtos.medicineDtos.AddMedicineDto;
import com.example.nabd.dtos.BasisResponse;

public interface IPatientMedicineService {
    BasisResponse update(Long id, AddMedicineDto addMedicineDto);
    BasisResponse deleteMedicineBySpecialization(Long patientId , Long specializationId);
    BasisResponse delete(Long id);
}
