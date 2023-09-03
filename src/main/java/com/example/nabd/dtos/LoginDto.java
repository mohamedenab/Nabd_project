package com.example.nabd.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class LoginDto {
    @NotEmpty(message = "Email cant be empty")
    private String email;
    @NotEmpty(message = "password cant be empty")
    private String password;
}
