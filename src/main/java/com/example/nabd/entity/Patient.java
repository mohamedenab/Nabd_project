package com.example.nabd.entity;

import com.example.nabd.enums.Insurance;
import com.example.nabd.enums.MaritalStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String nameOfParent;
    @Column(nullable = false)
    private String nationalID;
    @Column(nullable = false)
    private int numberOfFamilyMembers;
    private String nationalIDForParent;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
    @ElementCollection
    @CollectionTable(name = "user_phone_numbers", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "phone_number" ,nullable = false)
    private List<String> mobileNumbers;
    @Column(nullable = false)
    private String address;
    private String addressLink;
    private String volunteerName;
    private String volunteerMobileNumber;
    private String periodOfDiscovery;
    private String dateOfBeginningOfDecision;
    private String dateOfHelp;
    private String discoveryDetailsWithImageLink;
    @Enumerated(EnumType.STRING)
    private Insurance thereInsurance;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Locations locations;
    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;
}
