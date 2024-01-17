package com.example.nabd.dtos;

import com.example.nabd.dtos.authDtos.UserDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDto {
    private Long id;
    @NotEmpty(message = "Location name cant be empty")
    private String locationName;
    private List<UserDto> userDtos;
}
