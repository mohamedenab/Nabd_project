package com.example.nabd.entity;

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
public class Locations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String locationName;
    @ManyToMany(mappedBy = "locations", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();
    @OneToMany(mappedBy = "locations", fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Patient> patient = new ArrayList<>();
}
