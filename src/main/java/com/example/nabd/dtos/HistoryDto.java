package com.example.nabd.dtos;

import com.example.nabd.dtos.patientDtos.PatientDto;
import com.example.nabd.enums.HistoryType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    private String startAt;
    private String updatedAt;
    private PatientDto patientDto;

}
