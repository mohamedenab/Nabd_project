package com.example.nabd.mapper;

import com.example.nabd.dtos.LocationDto;
import com.example.nabd.dtos.patientDtos.PatientDto;
import com.example.nabd.dtos.SpecializationDto;
import com.example.nabd.entity.Locations;
import com.example.nabd.entity.Patient;
import com.example.nabd.entity.Specialization;
import com.example.nabd.exception.ResourceNotFoundException;
import com.example.nabd.repository.LocationsRepo;
import com.example.nabd.repository.SpecializationRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class PatientMapper {
    private final LocationsRepo locationsRepo;
    private final SpecializationRepo specializationRepo;

    public PatientMapper(LocationsRepo locationsRepo, SpecializationRepo specializationRepo) {
        this.locationsRepo = locationsRepo;
        this.specializationRepo = specializationRepo;
    }

    public Patient DtoToEntity(PatientDto patientDto){
        Locations locations = locationsRepo.findById(patientDto.getLocationsId()).orElseThrow(()-> new ResourceNotFoundException("Location" ,"id",patientDto.getLocationsId()));
        List<Specialization> specializations = specializationRepo.findAllById(patientDto.getSpecializations());
        return Patient.builder().id(patientDto.getId())
                .name(patientDto.getName()).nameOfParent(patientDto.getNameOfParent())
                .nationalID(patientDto.getNationalID()).numberOfFamilyMembers(patientDto.getNumberOfFamilyMembers())
                .nationalIDForParent(patientDto.getNationalIDForParent()).maritalStatus(patientDto.getMaritalStatus())
                .mobileNumbers(patientDto.getMobileNumbers()).address(patientDto.getAddress())
                .addressLink(patientDto.getAddressLink()).volunteerName(patientDto.getVolunteerName())
                .volunteerMobileNumber(patientDto.getVolunteerMobileNumber()).periodOfDiscovery(patientDto.getPeriodOfDiscovery())
                .dateOfBeginningOfDecision(patientDto.getDateOfBeginningOfDecision())
                .dateOfHelp(patientDto.getDateOfHelp()).discoveryDetailsWithImageLink(patientDto.getDiscoveryDetailsWithImageLink())
                .active(patientDto.isActive()).thereInsurance(patientDto.getThereInsurance())
                .locationId(locations).specializations(specializations).active(patientDto.isActive()).build();
    }
    public PatientDto EntityToDto(Patient patient){
        List<Long> specializationsList =new ArrayList<>();
        List<SpecializationDto> specializationDtoList = new ArrayList<>();
        LocationDto locationDto = LocationDto.builder().locationName(patient.getLocationId().getLocationName())
                .id(patient.getLocationId().getId()).build();
        for (Specialization s:
             patient.getSpecializations()) {
            specializationsList.add(s.getId());
            SpecializationDto specializationDto = SpecializationDto.builder().id(s.getId()).name(s.getName()).build();
            specializationDtoList.add(specializationDto);
        }
        return PatientDto.builder().name(patient.getName()).nameOfParent(patient.getNameOfParent())
                .nationalID(patient.getNationalID()).numberOfFamilyMembers(patient.getNumberOfFamilyMembers())
                .nationalIDForParent(patient.getNationalIDForParent()).maritalStatus(patient.getMaritalStatus())
                .mobileNumbers(patient.getMobileNumbers()).address(patient.getAddress())
                .addressLink(patient.getAddressLink()).volunteerName(patient.getVolunteerName())
                .volunteerMobileNumber(patient.getVolunteerMobileNumber()).periodOfDiscovery(patient.getPeriodOfDiscovery())
                .dateOfBeginningOfDecision(patient.getDateOfBeginningOfDecision())
                .dateOfHelp(patient.getDateOfHelp()).discoveryDetailsWithImageLink(patient.getDiscoveryDetailsWithImageLink())
                .active(patient.isActive()).thereInsurance(patient.getThereInsurance())
                .locationsId(patient.getLocationId().getId()).specializations(specializationsList)
                .locationDto(locationDto).specializationDto(specializationDtoList).id(patient.getId()).build();

    }
}
