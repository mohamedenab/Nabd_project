package com.example.nabd.service.imp;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.LocationDto;
import com.example.nabd.dtos.UserDto;
import com.example.nabd.entity.Locations;
import com.example.nabd.exception.NabdAPIExeption;
import com.example.nabd.exception.ResourceNotFoundException;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.repository.LocationsRepo;
import com.example.nabd.service.ILocationsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LocationServiceImp implements ILocationsService {
    private final LocationsRepo locationsRepo;
    private final BasisResponseMapper basisResponseMapper = new BasisResponseMapper();

    public LocationServiceImp(LocationsRepo locationsRepo) {
        this.locationsRepo = locationsRepo;
    }

    @Override
    public BasisResponse getLocations(int pageNo, int pageSize, String sortBy) {
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Locations> locations = locationsRepo.findAll(pageable);
        List<Locations> locationsContent = locations.getContent();
        List<LocationDto> locationDtoList = locationsContent.stream().map(location -> LocationDto.builder()
                .id(location.getId()).locationName(location.getLocationName()).build()).toList();
        return basisResponseMapper.createBasisResponseForLocation(locationDtoList,pageNo,locations);
    }

    @Override
    public BasisResponse createLocation(LocationDto locationDto) {
        Locations locationEntity = Locations.builder().locationName(locationDto.getLocationName()).build();
        Locations savedlocations = locationsRepo.save(locationEntity);
        locationDto.setLocationName(savedlocations.getLocationName());
        locationDto.setId(savedlocations.getId());
        return basisResponseMapper.createBasisResponse(locationDto);
    }

    @Override
    public String DeleteLocation(Long id) {
        Locations locations = locationsRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Location","id",id));
        if (locations.getPatient().size()>0){
            throw new NabdAPIExeption("Location have patient", HttpStatus.BAD_REQUEST);
        }
        locationsRepo.delete(locations);
        return "Location deleted successfully";
    }

    @Override
    public BasisResponse getUserRelatedToLocation(Long id) {
        Locations locations = locationsRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Location","id",id));
        List<UserDto> userDtolist = locations.getUsers().stream().map(user -> UserDto.builder()
                .name(user.getName()).phoneNumber(user.getPhoneNumber())
                .email(user.getEmail()).id(user.getId()).build()).toList();
        return basisResponseMapper.createBasisResponse(userDtolist);
    }

    @Override
    public BasisResponse getLocation(Long locationId, String pageNo, String pageSize, String sortBy, String filter) {
//        Locations locations = locationsRepo.findById(locationId).orElseThrow(() -> new ResourceNotFoundException("Location","id",id));
//
        return null;
    }

    @Override
    public BasisResponse updateLocation(Long id, LocationDto locationDto) {
        Locations locations = locationsRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Location","id",id));
        locations.setLocationName(locationDto.getLocationName());
        Locations savedLocation = locationsRepo.save(locations);
        LocationDto locationDto1 = LocationDto.builder().locationName(savedLocation.getLocationName()).build();
        return basisResponseMapper.createBasisResponse(locationDto1);
    }
}
