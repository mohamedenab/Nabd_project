package com.example.nabd.controller;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.service.IReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/report")
@Tag(
        name = "report api"
)
public class ReportController {
    private final IReportService reportService;

    public ReportController(IReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU')")
    ResponseEntity<BasisResponse> createReport(){
        return ResponseEntity.ok(reportService.createReport());
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU')")
    ResponseEntity<BasisResponse> getReport(){
        return ResponseEntity.ok(reportService.getReport());
    }
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU')")
    ResponseEntity<BasisResponse> DeleteReport(){
        return ResponseEntity.ok(reportService.deleteReport());
    }
}
