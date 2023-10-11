package com.example.nabd.repository;

import com.example.nabd.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface HistoryRepo extends JpaRepository<History,Long> {
}
