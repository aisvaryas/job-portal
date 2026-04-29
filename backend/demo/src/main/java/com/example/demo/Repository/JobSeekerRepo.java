package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Jobseeker;
import com.example.demo.Entity.User;


public interface JobSeekerRepo extends JpaRepository<Jobseeker,Long>{

    Optional<Jobseeker> findByUserEmail(String userEmail);
    Optional<Jobseeker> findByUser(User user);
    
}
