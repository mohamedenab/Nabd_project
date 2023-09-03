package com.example.nabd.entity;

import com.example.nabd.enums.Roles;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(length = 50,nullable = false)
    private String name;
    private String phoneNumber;
    @Email(message = "Please provide a valid email address")
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles roles = Roles.ROLE_NU;
    @Column(nullable = false)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{4,}$",
            message = "Password must have at least 4 characters with letters and numbers")
    private String password;
    private Date createdAt = new Date();
    private Date passwordUpdatedAt;

}
