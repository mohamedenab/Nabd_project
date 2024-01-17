package com.example.nabd.service;

import com.example.nabd.dtos.BasisResponse;

public interface IPrintService {
    BasisResponse printAll();
    BasisResponse printLocation(Long id);
    BasisResponse printPatient(Long id);
}
