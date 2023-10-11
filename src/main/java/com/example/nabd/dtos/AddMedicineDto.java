package com.example.nabd.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Date;
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
    @NotNull(message = "startIn is empty")
    Date startIn;
    String notes;
}
