package com.example.nabd.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UserDto {
    @NotEmpty(message = "User name can't be empty")
    private String name;
    @NotEmpty(message = "User name can't be empty")
    @Email(message = "Please provide a valid email address")
    private String email;
    String phoneNumber;
}
