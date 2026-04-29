package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Employer;
import com.example.demo.Entity.User;

public interface  EmployerRepo extends JpaRepository<Employer, Long>{
    Optional<Employer> findByUser(User user);
    Optional<Employer> findByUserEmail(String email);
}
