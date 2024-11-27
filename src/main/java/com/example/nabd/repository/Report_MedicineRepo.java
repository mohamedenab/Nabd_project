package com.example.nabd.repository;

import com.example.nabd.entity.Report_Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Report_MedicineRepo extends JpaRepository<Report_Medicine,Long> {
    Report_Medicine findByMedicineId(Long id);

    @Query("SELECT SUM(rm.totalPrice) FROM Report_Medicine rm")
    Double calculateTotalPriceSum();

}
