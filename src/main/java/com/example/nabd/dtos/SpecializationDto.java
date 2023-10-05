package com.example.nabd.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpecializationDto {
    private Long id;
    @NotEmpty(message = "name can't be empty")
    private String name;
}
