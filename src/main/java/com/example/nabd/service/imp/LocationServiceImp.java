package com.example.nabd.service.imp;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.LocationDto;
import com.example.nabd.entity.Locations;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.repository.LocationsRepo;
import com.example.nabd.service.ILocationsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public BasisResponse getLocation(int pageNo, int pageSize, String sortBy) {
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Locations> locations = locationsRepo.findAll(pageable);
        List<Locations> locationsContent = locations.getContent();
        List<LocationDto> locationDtoList = locationsContent.stream().map(location -> LocationDto.builder()
                .locationName(location.getLocationName()).build()).toList();
        return basisResponseMapper.createBasisResponseForLocation(locationDtoList,pageNo,locations);
    }

    @Override
    public BasisResponse createLocation(LocationDto locationDto) {

        Locations locationEntity = Locations.builder().locationName(locationDto.getLocationName()).build();
        Locations savedlocations = locationsRepo.save(locationEntity);
        locationDto.setLocationName(savedlocations.getLocationName());
        return basisResponseMapper.createBasisResponse(locationDto);
    }
}
