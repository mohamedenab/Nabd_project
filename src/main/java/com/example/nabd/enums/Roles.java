package com.example.nabd.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Roles {
    ROLE_NU("Normal user"),
    ROLE_AU("Admin user"),
    ROLE_SU("Super admin");
    private final String desc;
}
