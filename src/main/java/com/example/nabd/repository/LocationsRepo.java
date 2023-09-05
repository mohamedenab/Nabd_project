package com.example.nabd.repository;

import com.example.nabd.entity.Locations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationsRepo extends JpaRepository<Locations,Long> {
    Locations findByLocationName(String locationName);
}
