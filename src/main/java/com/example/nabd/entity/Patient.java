package com.example.nabd.entity;

import com.example.nabd.enums.Insurance;
import com.example.nabd.enums.MaritalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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
    @CollectionTable(name = "patient_phone_numbers", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "phone_number" ,nullable = false)
    private List<String> mobileNumbers;
    @Column(nullable = false ,columnDefinition = "TEXT")
    @Lob
    private String address;
    private String addressLink;
    private String volunteerName;
    private String volunteerMobileNumber;
    private String periodOfDiscovery;
    private String dateOfBeginningOfDecision;
    private String dateOfHelp;
    private String likGovernmentExpense;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String discoveryDetailsWithImageLink;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String PatientDiagnosis;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String reasonForDeactivate;
    private boolean active = true;
    @Enumerated(EnumType.STRING)
    private Insurance thereInsurance;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Locations locationId;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "patient_specialization",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id"))
    List<Specialization> specializations = new ArrayList<>();
    @OneToMany(mappedBy = "patient",fetch = FetchType.LAZY)
    private List<Patient_Medicine> patientMedicines;
    @OneToMany(mappedBy = "patientH", fetch = FetchType.LAZY)
    private List<History> histories = new ArrayList<>();
    private boolean isDeleted;
}
