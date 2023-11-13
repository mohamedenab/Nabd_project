package com.example.nabd.controller;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.HistoryDto;
import com.example.nabd.service.IHistoryService;
import com.example.nabd.utility.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/history")
public class HistoryController {
    private final IHistoryService historyService;

    public HistoryController(IHistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping("/patient/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU','ROLE_NU')")
    public ResponseEntity<BasisResponse> createHistoryForUser(
            @PathVariable(name = "id") Long id , @Valid @RequestBody HistoryDto historyDto,
            @RequestParam(value = "year") int year,
            @RequestParam(value = "month") int month
    ){
        return new ResponseEntity<>(historyService.addHistory(historyDto,id,year,month), HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU','ROLE_NU')")
    public ResponseEntity<BasisResponse> getHistory(
            @PathVariable(name = "id") Long id){
        return ResponseEntity.ok(historyService.getHistoryById(id));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU')")
    public ResponseEntity<BasisResponse> updateHistory(
            @PathVariable(name = "id") Long id, @Valid @RequestBody HistoryDto historyDto){
        return ResponseEntity.ok(historyService.updateHistory(historyDto,id));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU')")
    public ResponseEntity<BasisResponse> deleteHistory(
            @PathVariable(name = "id") Long id){
        return ResponseEntity.ok(historyService.deleteHistory(id));
    }

}
