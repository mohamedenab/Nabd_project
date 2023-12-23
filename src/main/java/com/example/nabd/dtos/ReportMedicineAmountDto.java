package com.example.nabd.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportMedicineAmountDto {
    int numberBox;
    int numberPastille;
}
