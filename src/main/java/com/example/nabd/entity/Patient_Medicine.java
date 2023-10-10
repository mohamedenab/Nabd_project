package com.example.nabd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient_Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "patient_id" , nullable = false)
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "medicine_id" , nullable = false)
    private Medicine medicine;
    @ElementCollection
    @CollectionTable(name = "Patient_Medicine_List_month", joinColumns = @JoinColumn(name = "Patient_Medicine_id"))
    @Column(name = "month" ,nullable = false)
    private List<Integer> month;
    Long specialization;
    int numberBox;
    int numberPastille;
    int Repetition;
    Date startIn;
}
