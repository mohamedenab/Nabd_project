package com.example.nabd.dtos.reportDtos;

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
