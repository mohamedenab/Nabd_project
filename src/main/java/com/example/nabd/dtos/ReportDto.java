package com.example.nabd.dtos;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDto {
    Long id;
    List<ReportMedicineDto> reportMedicineDto;
}
