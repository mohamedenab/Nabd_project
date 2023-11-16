package com.example.nabd.dtos;

import com.example.nabd.entity.Locations;
import com.example.nabd.enums.Insurance;
import com.example.nabd.enums.MaritalStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class PatientDto {

    private Long id;
    @NotEmpty(message = "Patient name can't be empty")
    private String name;
    private String nameOfParent;
    @NotEmpty(message = "Patient NationalID can't be empty")
    private String nationalID;
    @NotNull(message = "numberOfFamilyMembers can't be empty")
    @Min(value = 0)
    @Max(value = 15)
    private int numberOfFamilyMembers;
    private String nationalIDForParent;
    private MaritalStatus maritalStatus;
    @NotEmpty
    private List<String> mobileNumbers = new ArrayList<>();
    @NotEmpty(message = "Address can't be empty")
    private String address;
    private String addressLink;
    private String volunteerName;
    private String volunteerMobileNumber;
    private String periodOfDiscovery;
    private String dateOfBeginningOfDecision;
    private String dateOfHelp;
    private String discoveryDetailsWithImageLink;
    private Insurance thereInsurance;
    private Long locationsId;
    private List<Long> specializations = new ArrayList<>();
    private LocationDto locationDto;
    private List<SpecializationDto> specializationDto;
    private boolean active;

}
