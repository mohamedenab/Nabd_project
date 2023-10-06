package com.example.nabd.service.imp;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.LocationDto;
import com.example.nabd.dtos.PatientDto;
import com.example.nabd.dtos.UserDto;
import com.example.nabd.entity.Locations;
import com.example.nabd.entity.Patient;
import com.example.nabd.entity.User;
import com.example.nabd.exception.NabdAPIExeption;
import com.example.nabd.exception.ResourceNotFoundException;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.repository.LocationsRepo;
import com.example.nabd.repository.PatientRepo;
import com.example.nabd.repository.UserRepo;
import com.example.nabd.service.ILocationsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServiceImp implements ILocationsService {
    private final LocationsRepo locationsRepo;
    private final UserRepo userRepo;
    private final PatientRepo patientRepo;
    private final BasisResponseMapper basisResponseMapper = new BasisResponseMapper();

    public LocationServiceImp(LocationsRepo locationsRepo, UserRepo userRepo, PatientRepo patientRepo) {
        this.locationsRepo = locationsRepo;
        this.userRepo = userRepo;
        this.patientRepo = patientRepo;
    }

    @Override
    public BasisResponse getLocations(int pageNo, int pageSize, String sortBy) {
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Object[] authorize =SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray();
        System.out.println(authorize[0]);
        if (authorize[0].toString().equals("ROLE_NU")){
            User user = userRepo.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            List<Locations> locationsContent = user.getLocations();
            List<LocationDto> locationDtoList = locationsContent.stream().map(location -> LocationDto.builder()
                    .id(location.getId()).locationName(location.getLocationName()).build()).toList();
            return basisResponseMapper.createBasisResponse(locationDtoList);
        }
        Page<Locations> locations = locationsRepo.findAll(pageable);
        List<Locations> locationsContent = locations.getContent();
        List<LocationDto> locationDtoList = locationsContent.stream().map(location -> LocationDto.builder()
                .id(location.getId()).locationName(location.getLocationName())
                .userDtos(location.getUsers().stream().map(
                        user -> UserDto.builder().name(user.getName()).build())
                        .collect(Collectors.toList())).build()).toList();
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
    public BasisResponse getPatientRelatedToLocation(Long id ,int pageNo, int pageSize, String sortBy) {
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Locations locations = locationsRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Location","id",id));
        Page<Patient> patientList = patientRepo.findByLocations(locations , pageable);
        List<Patient> patients = patientList.getContent();
        List<PatientDto> patientDtoList = patients.stream().map(patient -> PatientDto.builder()
                .name(patient.getName()).mobileNumbers(patient.getMobileNumbers())
                .id(patient.getId()).build()).toList();
        return basisResponseMapper.createBasisResponseForPatient(patientDtoList,pageNo,patientList);
    }

    @Override
    public BasisResponse updateLocation(Long id, LocationDto locationDto) {
        Locations locations = locationsRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Location","id",id));
        locations.setLocationName(locationDto.getLocationName());
        Locations savedLocation = locationsRepo.save(locations);
        LocationDto locationDto1 = LocationDto.builder().locationName(savedLocation.getLocationName()).build();
        return basisResponseMapper.createBasisResponse(locationDto1);
    }

    @Override
    public BasisResponse convertPatientLocationToAnother(Long from, Long to) {
        Locations locationsFrom = locationsRepo.findById(from).orElseThrow(() -> new ResourceNotFoundException("Location","id",from));
        Locations locationsTo = locationsRepo.findById(to).orElseThrow(() -> new ResourceNotFoundException("Location","id",to));
        List<Patient> patientsFrom = locationsFrom.getPatient();
        for (Patient pa :patientsFrom) {
            pa.setLocations(locationsTo);
            patientRepo.save(pa);
        }
        locationsTo = locationsRepo.findById(to).orElseThrow(() -> new ResourceNotFoundException("Location","id",to));
        LocationDto locationDto = LocationDto.builder()
                .id(locationsTo.getId()).locationName(locationsTo.getLocationName())
                .userDtos(locationsTo.getUsers().stream().map(
                                user -> UserDto.builder().name(user.getName()).build())
                        .collect(Collectors.toList())).build();

        return basisResponseMapper.createBasisResponse(locationDto);
    }
}
