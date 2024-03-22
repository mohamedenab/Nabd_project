package com.example.nabd.controller;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.medicineDtos.MedicineDto;
import com.example.nabd.service.IMedicineService;
import com.example.nabd.utility.AppConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin()
@RequestMapping("/api/v1/medicine")
@Tag(name = "Medicine service apis ")
public class MedicineController {
    private final IMedicineService medicineService;

    public MedicineController(IMedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU')")
    public ResponseEntity<BasisResponse> addNewMedicine(@Valid @RequestBody MedicineDto medicineDto) {
        return new ResponseEntity<>(medicineService.create(medicineDto), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU')")
    public ResponseEntity<BasisResponse> updateMedicine(
            @PathVariable(name = "id")Long id,
            @Valid @RequestBody MedicineDto medicineDto) {
        return new ResponseEntity<>(medicineService.update(id,medicineDto), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU','ROLE_NU')")
    public ResponseEntity<BasisResponse> getMedicines(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "nameInEng", required = false) String sortBy,
            @RequestParam(value = "filter", required = false) String filter) {
        return ResponseEntity.ok(medicineService.getMedicine(pageNo, pageSize, sortBy, filter));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU')")
    public ResponseEntity<BasisResponse> getMedicineName(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(medicineService.getMedicineNameById(id));
    }

    @GetMapping("/patient/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU')")
    public ResponseEntity<BasisResponse> getPatientOfThisMedicine(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(medicineService.getPatientMedicine(id));
    }
    @PutMapping("/{medicineId}/patient/{patientId}/deactivate")
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU','ROLE_NU')")
    public ResponseEntity<BasisResponse> deactivatemedicine(
            @PathVariable(name = "medicineId") Long medicineId,
            @PathVariable(name = "patientId") Long patientId){
        return ResponseEntity.ok(medicineService.deactivatemedicine(medicineId,patientId));
    }
    @PutMapping("/{medicineId}/patient/{patientId}/activate")
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU','ROLE_NU')")
    public ResponseEntity<BasisResponse> activatemedicine(
            @PathVariable(name = "medicineId") Long medicineId,
            @PathVariable(name = "patientId") Long patientId){
        return ResponseEntity.ok(medicineService.activatemedicine(medicineId,patientId));
    }
    @GetMapping("/{firstId}/{secondId}")
    @PreAuthorize("hasRole('ROLE_SU')")
    public ResponseEntity<BasisResponse> replaceMedicineWithAnother(
            @PathVariable(name = "firstId") Long firstId, @PathVariable(name = "secondId") Long secondId) {
        return ResponseEntity.ok(medicineService.replaceMedicineWithAnother(firstId, secondId));

    }

    @DeleteMapping("/{medicineId}/patient/{patientId}")
    @PreAuthorize("hasRole('ROLE_SU')")
    public ResponseEntity<BasisResponse> deleteMedicineFromPatient(
            @PathVariable(name = "medicineId") Long medicineId,
            @PathVariable(name = "patientId") Long patientId) {
        return ResponseEntity.ok(medicineService.removeMedicineFromPatient(medicineId, patientId));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_SU')")
    public ResponseEntity<Object> deleteMedicineByName(@PathVariable(name = "id") Long medicineid) {
        return ResponseEntity.ok(medicineService.delete(medicineid));
    }

}
