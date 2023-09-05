package com.example.nabd.service;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.LocationDto;

public interface ILocationsService {
    BasisResponse getLocation(int pageNo, int pageSize, String sortBy);
    BasisResponse createLocation(LocationDto locationDto);
}
