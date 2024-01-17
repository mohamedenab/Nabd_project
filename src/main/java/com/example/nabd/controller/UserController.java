package com.example.nabd.controller;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.authDtos.RegisterDto;
import com.example.nabd.service.IUserService;
import com.example.nabd.utility.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin()
@RequestMapping("/api/v1/users")
@Tag(
        name = "User service api  "
)
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }
    @Operation(
            summary = "get users function ",
            description = "used pagination and sort to get user" +
                    "and filter string is user role to filter user list "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @GetMapping
    @PreAuthorize("hasRole('ROLE_SU')")
    public ResponseEntity<BasisResponse> getUsers(
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy" ,defaultValue = AppConstants.DEFAULT_SORT_BY , required = false) String sortBy ,
            @RequestParam(value = "filter" ,required = false) String filter
    ){
        return ResponseEntity.ok(userService.getUsers(pageNo,pageSize,sortBy,filter));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SU')")
    public  ResponseEntity<BasisResponse> editeUser(@PathVariable Long id ,@Valid @RequestBody RegisterDto registerDto){
        return new ResponseEntity<>(userService.updateUser(id,registerDto),HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SU')")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }

}
