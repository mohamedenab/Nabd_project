package com.example.nabd.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Report_Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String medicine;
    Long medicineId;
    int numberBox;
    int numberPastille;
    double totalPrice;
    @ManyToOne
    @JoinColumn(name = "report_id" , nullable = false)
    private Report report;
}
