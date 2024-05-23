package com.example.nabd.dtos.printDtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientPrintDto {
    private String name;
    private List<String> phoneNumber;
    private String volunteerName;
    private List<MedicinePrintDto> medicinePrintDtos;
    private String dateOfBeginningOfDecision;
}
