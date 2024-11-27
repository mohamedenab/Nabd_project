package com.example.nabd.dtos.reportDtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticsDto {
    long numberOfPatient;
    long reportMedicinesCount;
    double price;
}
