package com.example.nabd.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

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
