package com.example.nabd.dtos;

import com.example.nabd.enums.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;


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
    @Size(min = 4 ,message = "Password must have more than 4 characters")
    private String password;
    @NotNull(message = "User type must be specified")
    private Roles roles;
    private String phoneNumber;
    private List<Long> locationListId;
}
