package com.example.nabd.repository;

import com.example.nabd.entity.Medicine;
import com.example.nabd.entity.Patient;
import com.example.nabd.entity.Patient_Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface Patient_MedicineRepo extends JpaRepository<Patient_Medicine,Long> {
    List<Patient_Medicine> findByPatientAndMedicine(Patient patient , Medicine medicine);
    List<Patient_Medicine> findByMedicineAndSpecialization(Medicine medicine , Long specialization);
    List<Patient_Medicine> findByPatientAndSpecialization(Patient patient , Long specialization);


}
