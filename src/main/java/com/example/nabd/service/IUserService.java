package com.example.nabd.service;

import com.example.nabd.dtos.BasisResponse;

public interface IUserService {
    BasisResponse getUsers(int pageNo, int pageSize, String sortBy, String filter);
}
