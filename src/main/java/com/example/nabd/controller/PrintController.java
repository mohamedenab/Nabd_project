package com.example.nabd.controller;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.service.IPrintService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin()
@RequestMapping("/api/v1/print")
@Tag(
        name = "Print service apis "
)
public class PrintController {
    private final IPrintService printService;

    public PrintController(IPrintService printService) {
        this.printService = printService;
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU','ROLE_NU')")
    public ResponseEntity<BasisResponse> getAllData(){
        return ResponseEntity.ok(printService.printAll());
    }
    @GetMapping("/location/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU','ROLE_NU')")
    public ResponseEntity<BasisResponse> getLocationData(@PathVariable(name = "id") Long id){
        return  ResponseEntity.ok(printService.printLocation(id));
    }
    @GetMapping("/patient/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU','ROLE_NU')")
    public ResponseEntity<BasisResponse> getPatientData(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(printService.printPatient(id));
    }
}
