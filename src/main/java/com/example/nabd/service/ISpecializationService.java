package com.example.nabd.service;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.RegisterDto;
import com.example.nabd.dtos.SpecializationDto;

public interface ISpecializationService {
    BasisResponse createSpecialization(SpecializationDto specializationDto);
    BasisResponse getSpecializations();
    BasisResponse updateSpecialization(Long id , SpecializationDto specializationDto);
    String deleteMedicineBySpecialization(Long medicineId , Long specializationId);
    String deleteSpecialization(Long id);
    BasisResponse deactivatemedicine(Long specializationId, Long patientId);
    BasisResponse activatemedicine(Long specializationId, Long patientId);


}
