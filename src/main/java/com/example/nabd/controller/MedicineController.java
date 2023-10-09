package com.example.nabd.controller;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.MedicineDto;
import com.example.nabd.service.IMedicineService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("medicine")
@Tag(
        name = "Medicine service apis "
)
public class MedicineController {
    private final IMedicineService medicineService;

    public MedicineController(IMedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU')")
    public ResponseEntity<BasisResponse> addNewMedicine(@Valid @RequestBody MedicineDto medicineDto){
        return new ResponseEntity<>(medicineService.create(medicineDto), HttpStatus.CREATED);
    }
    @DeleteMapping("/{name}")
    @PreAuthorize("hasRole('ROLE_SU')")
    public ResponseEntity<String> deleteMedicineByName(@PathVariable(name = "name") String medicineName){
        return ResponseEntity.ok(medicineService.delete(medicineName));
    }


}
