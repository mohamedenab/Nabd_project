package com.example.nabd.repository;

import com.example.nabd.entity.Locations;
import com.example.nabd.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepo extends JpaRepository<Patient,Long> {
    Page<Patient> findByLocationId(Locations locations , Pageable pageable);
}
