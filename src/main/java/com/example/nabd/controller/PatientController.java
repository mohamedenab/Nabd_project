package com.example.nabd.controller;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.PatientDto;
import com.example.nabd.service.IPatientService;
import com.example.nabd.utility.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patient")
@Tag(
        name = "Patient service apis "
)
public class PatientController {
    private final IPatientService patientService;

    public PatientController(IPatientService patientService) {
        this.patientService = patientService;
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU','ROLE_NU')")
    @Operation(
            summary = "create Patient "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 Create"
    )
    public ResponseEntity<BasisResponse> createPatient(@Valid @RequestBody PatientDto patientDto){
        return new ResponseEntity<>(patientService.createPatient(patientDto), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU','ROLE_NU')")
    public ResponseEntity<BasisResponse> getPatient(
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy" ,defaultValue = AppConstants.DEFAULT_SORT_BY , required = false) String sortBy ,
            @RequestParam(value = "filterType" ,required = false) String filterType ,
            @RequestParam(value = "filterValue" , required = false) String filterValue
    ){
        return ResponseEntity.ok(patientService.getPatient(pageNo,pageSize,sortBy,filterType,filterValue));
    }
}
