package com.example.nabd.service;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.RegisterDto;
import com.example.nabd.dtos.SpecializationDto;

public interface ISpecializationService {
    BasisResponse createSpecialization(SpecializationDto specializationDto);
    BasisResponse getSpecializations();
    BasisResponse updateSpecialization(Long id , SpecializationDto specializationDto);
    String deleteSpecialization(Long id);
}
