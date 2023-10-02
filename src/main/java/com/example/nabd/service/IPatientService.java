package com.example.nabd.service;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.PatientDto;

public interface IPatientService {
    BasisResponse createPatient(PatientDto patientDto);
    BasisResponse getPatient(int pageNo, int pageSize, String sortBy,  String filterType , String filterValue);
}
