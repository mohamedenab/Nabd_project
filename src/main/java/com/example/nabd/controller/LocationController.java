package com.example.nabd.controller;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.LocationDto;
import com.example.nabd.service.ILocationsService;
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
@RequestMapping("/api/v1/locations")
@Tag(
        name = "Locations service apis "
)
public class LocationController {
    private final ILocationsService locationsService;

    public LocationController(ILocationsService locationsService) {
        this.locationsService = locationsService;
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU','ROLE_NU')")
    @Operation(
            summary = "get location function ",
            description = "used pagination and sort to get locations"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    public ResponseEntity<BasisResponse> getLocations(
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy" ,defaultValue = "locationName" , required = false) String sortBy
    ){
        return ResponseEntity.ok(locationsService.getLocations(pageNo, pageSize, sortBy));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_SU')")
    @Operation(
            summary = "create location ",
            description = "Create locations by locationName and only super admin " +
                    "can create it"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 Create"
    )
    public ResponseEntity<BasisResponse> createLocation(@Valid @RequestBody LocationDto locationDto){
        return new ResponseEntity<>(locationsService.createLocation(locationDto), HttpStatus.CREATED);
    }
    @GetMapping("/{id}/patients")
    @PreAuthorize("hasAnyRole('ROLE_SU','ROLE_AU','ROLE_NU')")
    public ResponseEntity<BasisResponse> getPatients(
            @PathVariable(name = "id") Long locationId ,
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy" ,defaultValue = "name" , required = false) String sortBy
    ){
        return ResponseEntity.ok(locationsService.getPatientRelatedToLocation(locationId,pageNo,pageSize,sortBy));
    }
    @GetMapping("/{from}/{to}")
    @PreAuthorize("hasRole('ROLE_SU')")
    public ResponseEntity<BasisResponse> convertPatientsFromLocationToAnother(
            @PathVariable(name = "from") Long from , @PathVariable(name = "to") Long to){
        return ResponseEntity.ok(locationsService.convertPatientLocationToAnother(from,to));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SU')")
    public ResponseEntity<BasisResponse> updateLocation(
            @PathVariable(name = "id") Long locationId,
            @Valid @RequestBody LocationDto locationDto){
        return ResponseEntity.ok(locationsService.updateLocation(locationId,locationDto));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SU')")
    public ResponseEntity<String> deleteLocation(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(locationsService.DeleteLocation(id));
    }
}
