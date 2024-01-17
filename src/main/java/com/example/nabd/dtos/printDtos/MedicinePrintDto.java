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
    int numberBox;
    int numberPastille;
    int repetition;

}
