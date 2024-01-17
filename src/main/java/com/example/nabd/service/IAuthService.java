package com.example.nabd.service;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.authDtos.LoginDto;
import com.example.nabd.dtos.authDtos.RegisterDto;

public interface IAuthService {
    BasisResponse Signup(RegisterDto registerDto);
    BasisResponse login(LoginDto loginDto);
}
