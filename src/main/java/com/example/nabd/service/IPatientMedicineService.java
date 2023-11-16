package com.example.nabd.service;

import com.example.nabd.dtos.AddMedicineDto;
import com.example.nabd.dtos.BasisResponse;

public interface IPatientMedicineService {
    BasisResponse update(Long id, AddMedicineDto addMedicineDto);
    BasisResponse delete(Long id);
}
