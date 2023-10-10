package com.example.nabd.entity;

import com.example.nabd.enums.MedicineStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true ,nullable = false)
    private String nameInEng;
    private String nameInArb;
    @Column(nullable = false)
    private  double price;
    @Column(nullable = false)
    private int numberOfPastilleInEachBox;
    @Column(nullable = false)
    private String activeSubstance;
    @Column(nullable = false)
    private int numberOfPatientTakeIt;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MedicineStatus medicineStatus;
//    @ManyToMany(mappedBy = "medicines", fetch = FetchType.LAZY)
//    private List<Patient> patients = new ArrayList<>();
    @OneToMany(mappedBy = "medicine",fetch = FetchType.LAZY)
    private List<Patient_Medicine> patientMedicines;
}
