package com.example.nabd.dtos;

import com.example.nabd.enums.HistoryType;
import com.example.nabd.enums.MaritalStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class HistoryDto {
    private Long id;
    @NotNull
    private HistoryType historyType;
    private String comment;
    private String link;
    private PatientDto patientDto;

}
