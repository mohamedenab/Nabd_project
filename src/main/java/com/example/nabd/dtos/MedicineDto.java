package com.example.nabd.dtos;

import com.example.nabd.entity.Patient;
import com.example.nabd.enums.MedicineStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MedicineDto {
    private Long id;
    @NotEmpty(message = "Medicine name in English can't be empty ")
    private String nameInEng;
    @NotEmpty(message = "Medicine name in Arabic can't be empty ")
    private String nameInArb;
    @NotNull(message = "Medicine price can't be empty")
    private  double price;
    @NotNull(message = "Medicine price can't be empty")
    private int numberOfPastilleInEachBox;
    private String activeSubstance;
    private int numberOfPatientTakeIt;
    @Enumerated(EnumType.STRING)
    private MedicineStatus medicineStatus;
    private List<Patient> patients = new ArrayList<>();
}
