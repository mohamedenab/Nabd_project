package com.example.nabd.repository;

import com.example.nabd.entity.Medicine;
import com.example.nabd.enums.MedicineStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepo extends JpaRepository<Medicine,Long> {
    Medicine findByNameInEng(String name);
//    @Modifying
//    @Query(value = "UPDATE Medicine SET medicine_status = 'NOT_UPDATED'", nativeQuery = true)
//    void makeAllMedicineNotUpdated();

    @Modifying
//    @Transactional
    @Query("UPDATE Medicine m SET m.medicineStatus = 'NOT_UPDATED'")
    void makeAllMedicineNotUpdated();

    @Transactional
    @Modifying
    @Query("UPDATE Medicine m SET m.medicineStatus = :medicineStatus")
    void updateMedicineByMedicineStatus(@Param("medicineStatus") MedicineStatus medicineStatus);
    Page<Medicine> findByNameInEngContaining(String nameInEng, Pageable pageable);


}