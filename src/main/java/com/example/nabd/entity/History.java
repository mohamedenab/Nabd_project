package com.example.nabd.entity;

import com.example.nabd.enums.HistoryType;
import com.example.nabd.enums.MaritalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Date startDate= new Date();
    @Column(nullable = false)
    private Date updatedAt = new Date();
    private String comment;
    private String link;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HistoryType historyType;
    @ManyToOne
    @JoinColumn(name = "patient_id" , nullable = false)
    private Patient patientH;

}
