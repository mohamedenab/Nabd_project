package com.example.nabd.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientMedicineDto {
    @NotNull
    Long specialization;
    @NotNull(message = "numberBox is empty")
    int numberBox;
    @NotNull(message = "numberPastille is empty")
    int numberPastille;
    @NotNull(message = "Repetition is empty")
    int Repetition;
    @NotNull(message = "startIn is empty")
    Date startIn;
    List<Integer> arrayOfMonths = new ArrayList<>();
}