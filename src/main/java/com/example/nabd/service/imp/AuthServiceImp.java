package com.example.nabd.service.imp;

import com.example.nabd.dtos.*;
import com.example.nabd.entity.Locations;
import com.example.nabd.entity.User;
import com.example.nabd.exception.ResourceNotFoundException;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.repository.LocationsRepo;
import com.example.nabd.repository.UserRepo;
import com.example.nabd.security.JwtTokenProvider;
import com.example.nabd.service.IAuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServiceImp implements IAuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepository;
    private final LocationsRepo locationsRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final BasisResponseMapper basisResponseMapper = new BasisResponseMapper();

    public AuthServiceImp(AuthenticationManager authenticationManager,
                          UserRepo userRepository,
                          LocationsRepo locationsRepo, PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.locationsRepo = locationsRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public BasisResponse Signup(RegisterDto registerDto) {
        List<Locations> locationsArrayList= new ArrayList<>();
        if (registerDto.getLocationListId()!=null){
            for (Long locationId:registerDto.getLocationListId()) {
                Locations location= locationsRepo.findById(locationId).orElseThrow(()-> new ResourceNotFoundException("location" , "id",locationId));
                locationsArrayList.add(location);
            }
        }
        User user = User.builder().name(registerDto.getName()).email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword())).phoneNumber(registerDto.getPhoneNumber())
                .roles(registerDto.getRoles()).locations(locationsArrayList).build();
        userRepository.save(user);
        LoginDto loginDto = new LoginDto(registerDto.getEmail(), registerDto.getPassword());
        return login(loginDto);
    }

    @Override
    public BasisResponse login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail());
        if (user==null) throw new ResourceNotFoundException("User" , "email" ,loginDto.getEmail());
        Authentication authentication =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail() , loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(user.getEmail());
        JwtAuthnResponse jwrAuthnResponse;
        jwrAuthnResponse = JwtAuthnResponse.builder().accessToken(token)
                .role(user.getRoles()).userName(user.getName()).build();
        return basisResponseMapper.createBasisResponse(jwrAuthnResponse);
    }
}
