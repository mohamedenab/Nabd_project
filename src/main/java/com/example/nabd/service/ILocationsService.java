package com.example.nabd.service;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.LocationDto;

public interface ILocationsService {
    BasisResponse getLocations(int pageNo, int pageSize, String sortBy);
    BasisResponse createLocation(LocationDto locationDto);
    String DeleteLocation(Long id);
    BasisResponse getUserRelatedToLocation(Long id);
    BasisResponse getLocation(Long locationId ,String pageNo,String pageSize,String sortBy,String filter);
    BasisResponse updateLocation(Long id ,LocationDto locationDto);
    BasisResponse convertPatientLocationToAnother(Long from , Long to);
}
