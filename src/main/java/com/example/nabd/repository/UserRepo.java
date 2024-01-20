package com.example.nabd.repository;

import com.example.nabd.entity.Locations;
import com.example.nabd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    User findByEmail(String email);
    List<User> findByLocations(Locations location);

}
