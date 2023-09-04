package com.example.nabd.controller;

import com.example.nabd.dtos.UsersResponse;
import com.example.nabd.service.IUserService;
import com.example.nabd.utility.AppConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UsersResponse getUsers(
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy" ,defaultValue = AppConstants.DEFAULT_SORT_BY , required = false) String sortBy ,
            @RequestParam(value = "filter" ,required = false) String filter
    ){
        return userService.getUsers(pageNo,pageSize,sortBy,filter);
    }
}
