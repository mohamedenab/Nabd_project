package com.example.nabd.controller;

import com.example.nabd.dtos.JwtAuthnResponse;
import com.example.nabd.dtos.LoginDto;
import com.example.nabd.dtos.RegisterDto;
import com.example.nabd.service.IAuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthnResponse> loginFun(@Valid @RequestBody LoginDto loginDto){
        JwtAuthnResponse  jwtAuthnResponse = authService.login(loginDto);
        log.info("user "+loginDto.getEmail()+"is logged in");
        return ResponseEntity.ok(jwtAuthnResponse);
    }
    @PostMapping(value = {"signup" , "register"})
    public ResponseEntity<JwtAuthnResponse> registerFun(@Valid @RequestBody RegisterDto registerDto){
        JwtAuthnResponse  jwtAuthnResponse=authService.Signup(registerDto);
        log.info("user "+registerDto.getEmail()+"is register to our system");
        return new ResponseEntity<>(jwtAuthnResponse, HttpStatus.CREATED);
    }
}
