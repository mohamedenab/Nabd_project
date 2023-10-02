package com.example.nabd.dtos;

import com.example.nabd.enums.MaritalStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotEmpty(message = "Patient NationalID can't be empty")
    private String nationalID;
    @NotNull(message = "numberOfFamilyMembers can't be empty")
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
    private boolean thereInsurance;
    private Long locationsId;
    private Long SpecializationId;

}
