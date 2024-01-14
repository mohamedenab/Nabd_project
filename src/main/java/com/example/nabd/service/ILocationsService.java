package com.example.nabd.service;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.LocationDto;

public interface ILocationsService {
    BasisResponse getLocations(int pageNo, int pageSize, String sortBy);
    BasisResponse createLocation(LocationDto locationDto);
    BasisResponse getLocationById(Long id);
    BasisResponse DeleteLocation(Long id);
    BasisResponse getPatientRelatedToLocation(Long id , int pageNo, int pageSize, String sortBy);
    BasisResponse updateLocation(Long id ,LocationDto locationDto);
    BasisResponse convertPatientLocationToAnother(Long from , Long to);
}
