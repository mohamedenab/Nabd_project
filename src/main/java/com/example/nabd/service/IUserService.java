package com.example.nabd.service;

import com.example.nabd.dtos.UsersResponse;

public interface IUserService {
    UsersResponse getUsers(int pageNo,int pageSize,String sortBy,String filter);
}
