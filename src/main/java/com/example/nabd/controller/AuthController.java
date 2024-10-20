package com.example.nabd.controller;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.authDtos.LoginDto;
import com.example.nabd.dtos.authDtos.RegisterDto;
import com.example.nabd.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin()
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
        BasisResponse basisResponse = authService.login(loginDto);
        log.info("user "+loginDto.getEmail()+"is logged in");
        return ResponseEntity.ok(basisResponse);
    }
    @GetMapping("/login/1")
    public ResponseEntity<String> loginFun2(){
        return ResponseEntity.ok("tmam2");
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
        BasisResponse basisResponse = authService.Signup(registerDto);
        log.info("user "+registerDto.getEmail()+"is register to our system");
        return new ResponseEntity<>(basisResponse, HttpStatus.CREATED);
    }
}
