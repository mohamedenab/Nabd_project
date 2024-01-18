package com.example.nabd.dtos.printDtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicinePrintDto {
    private String medicineName;
    private int numberBox;
    private int numberPastille;
    private int repetition;
    private String notes;

}
