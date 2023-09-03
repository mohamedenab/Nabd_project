package com.example.nabd.service.imp;

import com.example.nabd.dtos.JwtAuthnResponse;
import com.example.nabd.dtos.LoginDto;
import com.example.nabd.dtos.RegisterDto;
import com.example.nabd.entity.User;
import com.example.nabd.exception.ResourceNotFoundException;
import com.example.nabd.repository.UserRepo;
import com.example.nabd.security.JwtTokenProvider;
import com.example.nabd.service.IAuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements IAuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImp(AuthenticationManager authenticationManager,
                          UserRepo userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public JwtAuthnResponse Signup(RegisterDto registerDto) {
        User user = User.builder().name(registerDto.getName()).email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword())).phoneNumber(registerDto.getPhoneNumber())
                .roles(registerDto.getRoles()).build();
        User savedUser = userRepository.save(user);
        LoginDto loginDto = new LoginDto(registerDto.getEmail(), registerDto.getPassword());
        return login(loginDto);
    }

    @Override
    public JwtAuthnResponse login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail());
        if (user==null) throw new ResourceNotFoundException("User" , "email" ,loginDto.getEmail());
        Authentication authentication =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail() , loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(user.getEmail());
        JwtAuthnResponse jwrAuthnResponse;
        jwrAuthnResponse = JwtAuthnResponse.builder().accessToken(token)
                .role(user.getRoles()).userName(user.getName()).build();
        return jwrAuthnResponse;
    }
}
