package com.example.nabd.entity;

import com.example.nabd.enums.Roles;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(length = 50,nullable = false)
    private String name;
    private String phoneNumber;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles roles = Roles.ROLE_NU;
    @Column(nullable = false)
    private String password;
    private Date createdAt = new Date();
    private Date passwordUpdatedAt;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_locations",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id"))
    List<Locations> locations = new ArrayList<>();


}
