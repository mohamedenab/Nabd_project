package com.example.nabd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Specialization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @OneToMany(mappedBy = "specialization", fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Patient> patient = new ArrayList<>();
}
