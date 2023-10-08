package com.example.nabd.service.imp;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.RegisterDto;
import com.example.nabd.dtos.UserDto;
import com.example.nabd.entity.Locations;
import com.example.nabd.entity.User;
import com.example.nabd.enums.Roles;
import com.example.nabd.exception.ResourceNotFoundException;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.repository.LocationsRepo;
import com.example.nabd.repository.UserRepo;
import com.example.nabd.service.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImp implements IUserService {
    private final UserRepo userRepo;
    private final LocationsRepo locationsRepo;
    private final PasswordEncoder passwordEncoder;

    private final BasisResponseMapper basisResponseMapper = new BasisResponseMapper();

    public UserServiceImp(UserRepo userRepo, LocationsRepo locationsRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.locationsRepo = locationsRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public BasisResponse getUsers(int pageNo,int pageSize,String sortBy,String filter) {
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<User> users = userRepo.findAll(pageable);
        List<User> userList = users.getContent();
        if (filter!=null){
            Roles.valueOf(filter);
            List<User> userFilterList = userList.stream().filter(user -> user.getRoles().name().equals(filter)).toList();
            List<UserDto> userDtolist = userFilterList.stream().map(user -> UserDto.builder()
                    .name(user.getName()).phoneNumber(user.getPhoneNumber())
                    .email(user.getEmail()).id(user.getId()).roles(user.getRoles()).build()).toList();
            return basisResponseMapper.createBasisResponseForUser(userDtolist,pageNo,users);
        }
        List<UserDto> userDtolist = userList.stream().map(user -> UserDto.builder()
                .name(user.getName()).phoneNumber(user.getPhoneNumber())
                .email(user.getEmail()).id(user.getId()).build()).toList();
        return basisResponseMapper.createBasisResponseForUser(userDtolist,pageNo,users);
    }

    @Override
    public BasisResponse updateUser(Long id, RegisterDto registerDto) {
        User user = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("user" , "id" , id));
        user.setEmail(registerDto.getEmail());
        user.setName(registerDto.getName());
        List<Locations> locationsArrayList= new ArrayList<>();
        for (Long locationId:registerDto.getLocationListId()) {
            Locations location= locationsRepo.findById(locationId).orElseThrow(()-> new ResourceNotFoundException("location" , "id",locationId));
            locationsArrayList.add(location);
        }
        user.setLocations(locationsArrayList);
        user.setPhoneNumber(registerDto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        UserDto userDto = UserDto.builder().id(user.getId()).locationsList(user.getLocations())
                .email(user.getEmail()).name(user.getName()).phoneNumber(user.getPhoneNumber()).build();
        return basisResponseMapper.createBasisResponse(userDto);
    }

    @Override
    public String deleteUser(Long id) {
        User user = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("user" , "id" , id));
        userRepo.delete(user);
        return "User delete successfully";
    }

}
