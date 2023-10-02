package com.example.nabd.controller;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.SpecializationDto;
import com.example.nabd.service.ISpecializationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/specialization")
public class SpecializationController {
    private final ISpecializationService specializationService;

    public SpecializationController(ISpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @PostMapping
    public ResponseEntity<BasisResponse> createSpecialization(@RequestBody SpecializationDto specializationDto){
        return new ResponseEntity<>(specializationService.createSpecialization(specializationDto) , HttpStatus.CREATED);
    }
}
