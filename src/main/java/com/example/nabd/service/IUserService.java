package com.example.nabd.service;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.authDtos.RegisterDto;

public interface IUserService {
    BasisResponse getUsers(int pageNo, int pageSize, String sortBy, String filter);
    BasisResponse updateUser(Long id , RegisterDto registerDto);
    String deleteUser(Long id);
}
