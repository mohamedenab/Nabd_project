package com.example.nabd.service;

import com.example.nabd.dtos.JwtAuthnResponse;
import com.example.nabd.dtos.LoginDto;
import com.example.nabd.dtos.RegisterDto;

public interface IAuthService {
    JwtAuthnResponse Signup(RegisterDto registerDto);
    JwtAuthnResponse login(LoginDto loginDto);
}
