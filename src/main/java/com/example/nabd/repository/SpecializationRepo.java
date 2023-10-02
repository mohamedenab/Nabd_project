package com.example.nabd.repository;

import com.example.nabd.entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface SpecializationRepo extends JpaRepository<Specialization,Long> {
}
