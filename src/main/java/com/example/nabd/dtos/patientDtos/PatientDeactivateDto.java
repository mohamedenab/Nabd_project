package com.example.nabd.dtos.patientDtos;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class PatientDeactivateDto {
    private String reasonForDeactivate;
}
