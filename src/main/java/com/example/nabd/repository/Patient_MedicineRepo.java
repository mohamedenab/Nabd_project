package com.example.nabd.repository;

import com.example.nabd.entity.Medicine;
import com.example.nabd.entity.Patient;
import com.example.nabd.entity.Patient_Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Patient_MedicineRepo extends JpaRepository<Patient_Medicine,Long> {
    Patient_Medicine findByPatientAndMedicine(Patient patient , Medicine medicine);
}
