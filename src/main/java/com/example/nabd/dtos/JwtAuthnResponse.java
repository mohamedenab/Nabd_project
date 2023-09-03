package com.example.nabd.dtos;

import com.example.nabd.enums.Roles;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtAuthnResponse {
    private String accessToken ;
    private String userName;
    private Roles role;
}
