package com.example.nabd.dtos.medicineDtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddMedicineDto {
    @NotNull
    Long specialization;
    @NotNull(message = "numberBox is empty")
    int numberBox;
    @NotNull(message = "numberPastille is empty")
    int numberPastille;
    @NotNull(message = "Repetition is empty")
    int repetition;
//    @NotNull(message = "startIn is empty")
//    Date startIn;
    int year;
    int month;
    String notes;
}
