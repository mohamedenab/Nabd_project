package com.example.nabd.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UserDto {
    @NotEmpty(message = "User name can't be empty")
    private String name;
    @NotEmpty(message = "User name can't be empty")
    @Email(message = "Please provide a valid email address")
    private String email;
    String phoneNumber;
}
