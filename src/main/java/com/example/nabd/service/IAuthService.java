package com.example.nabd.service;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.JwtAuthnResponse;
import com.example.nabd.dtos.LoginDto;
import com.example.nabd.dtos.RegisterDto;

public interface IAuthService {
    BasisResponse Signup(RegisterDto registerDto);
    BasisResponse login(LoginDto loginDto);
}
