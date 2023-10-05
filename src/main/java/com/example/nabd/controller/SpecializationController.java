package com.example.nabd.controller;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.SpecializationDto;
import com.example.nabd.service.ISpecializationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping
    public ResponseEntity<BasisResponse> getAllSpecialization(){
        return new ResponseEntity<>(specializationService.getSpecializations() , HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<BasisResponse> updateSpecialization(
            @PathVariable(name = "id") Long id ,
            @RequestBody SpecializationDto specializationDto){
        return new ResponseEntity<>(specializationService.updateSpecialization(id,specializationDto), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSpecialization(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(specializationService.deleteSpecialization(id), HttpStatus.OK);
    }
}
