package com.example.nabd.dtos;

import com.example.nabd.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    @NotEmpty(message = "User name can't be empty")
    private String name;
    @NotEmpty(message = "User name can't be empty")
    @Email(message = "Please provide a valid email address")
    private String email;
    @NotEmpty(message = "User password can't be empty")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{4,}$",
            message = "Password must have at least 4 characters with letters and numbers")
    private String Password;
    @NotNull(message = "User type must be specified")
    private Roles roles;
    private String phoneNumber;
}
