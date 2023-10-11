package com.example.nabd.service;

import com.example.nabd.dtos.AddMedicineDto;
import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.PatientDto;

public interface IPatientService {
    BasisResponse createPatient(PatientDto patientDto);
    BasisResponse getPatient(int pageNo, int pageSize, String sortBy,  String filterType , String filterValue);
    BasisResponse getPatientMedicine(Long id);
    BasisResponse getPatientHistory(Long id);
    BasisResponse getAllPatientMedicine(Long id);

    BasisResponse updatePatient(Long id ,PatientDto patientDto);
    BasisResponse addMedicine(Long medicineId , Long patientId , AddMedicineDto addMedicineDto);
    String deletePatient(Long id);
}
