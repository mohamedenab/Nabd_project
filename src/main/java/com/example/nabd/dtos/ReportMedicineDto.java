package com.example.nabd.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportMedicineDto {
    Long id;
    String medicine;
    Long medicineId;
    int numberBox;
    int numberPastille;
    double totalPrice;
}
