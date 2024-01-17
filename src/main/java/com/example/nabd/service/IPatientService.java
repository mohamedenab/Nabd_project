package com.example.nabd.service;

import com.example.nabd.dtos.medicineDtos.AddMedicineDto;
import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.patientDtos.PatientDto;

public interface IPatientService {
    BasisResponse createPatient(PatientDto patientDto);

    BasisResponse getPatient(int pageNo, int pageSize, String sortBy, String filterType, String filterValue);

    BasisResponse getPatientById(Long id);

    BasisResponse getPatientMedicine(Long id);

    BasisResponse getPatientHistory(Long id, int year, int month);

    BasisResponse getAllPatientMedicine(Long id);

    BasisResponse getPatientDateHistory(Long id);

    BasisResponse updatePatient(Long id, PatientDto patientDto);

    BasisResponse addMedicine(Long medicineId, Long patientId, AddMedicineDto addMedicineDto);

    String deletePatient(Long id);

    BasisResponse deactivatePatient(Long id);
    BasisResponse activatePatient(Long id);
}
