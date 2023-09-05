package com.example.nabd.controller;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.JwtAuthnResponse;
import com.example.nabd.dtos.LoginDto;
import com.example.nabd.dtos.RegisterDto;
import com.example.nabd.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
@Tag(
        name = "authenticator api  "
)
public class AuthController {
    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }
    @Operation(
            summary = "Login function "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @PostMapping("/login")
    public ResponseEntity<BasisResponse> loginFun(@Valid @RequestBody LoginDto loginDto){
        JwtAuthnResponse  jwtAuthnResponse = authService.login(loginDto);
        log.info("user "+loginDto.getEmail()+"is logged in");
        BasisResponse basisResponse = BasisResponse.builder().data(jwtAuthnResponse)
                .timestamp(new Date()).status("Success").build();
        return ResponseEntity.ok(basisResponse);
    }
    @Operation(
            summary = "Add new User",
            description = " Create User and save into database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201 CREATED"
    )
    @PostMapping(value = {"signup" , "register"})
    public ResponseEntity<BasisResponse> registerFun(@Valid @RequestBody RegisterDto registerDto){
        JwtAuthnResponse  jwtAuthnResponse=authService.Signup(registerDto);
        log.info("user "+registerDto.getEmail()+"is register to our system");
        BasisResponse basisResponse = BasisResponse.builder().data(jwtAuthnResponse)
                .timestamp(new Date()).status("Success").build();
        return new ResponseEntity<>(basisResponse, HttpStatus.CREATED);
    }
}
