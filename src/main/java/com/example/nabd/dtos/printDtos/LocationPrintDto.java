package com.example.nabd.dtos.printDtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationPrintDto {
    private String locationName;
    private List<PatientPrintDto> patientPrintDtos;
}
