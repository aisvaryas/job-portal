package com.example.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entity.Employer;
import com.example.demo.Entity.User;
import com.example.demo.Repository.EmployerRepo;
import com.example.demo.Repository.UserRepo;
import com.example.demo.dto.EmployerCreatedto;
import com.example.demo.mapper.EmployerMapper;

@Service
public class EmployerService {

    @Autowired
    private EmployerRepo repo;
    @Autowired
    private UserRepo userRepo;

    public Employer saveEmployer(EmployerCreatedto dto) {
        User user = userRepo.findById(dto.getUserId()).orElse(null);
        if(user == null) {
            return null;
        }
        Employer emp = EmployerMapper.toEntity(dto,user);
    return repo.save(emp);
}

    public List<Employer> getAllEmployers() {
        return repo.findAll();
    }

    public Employer getEmployerById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Employer getEmployerByEmail(String email) {
        return repo.findByUserEmail(email).orElse(null);
    }

    public Employer ensureEmployerByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new com.example.demo.exception.ResourceNotFoundException("User not found"));
        return ensureEmployerExists(user);
    }

    @Transactional
    public Employer ensureEmployerExists(User user) {
        return repo.findByUser(user).orElseGet(() -> {
            String emailPrefix = user.getEmail() == null ? "employer" : user.getEmail().split("@")[0];
            Employer emp = new Employer();
            emp.setUser(user);
            emp.setContactFirstName("Employer");
            emp.setContactLastName("User");
            emp.setCompanyName(emailPrefix + " Company");
            emp.setIndustry("");
            emp.setWebsite("");
            emp.setDescription("");
            emp.setPhone("");
            emp.setLocation("");
            emp.setVerifiedStatus("PENDING");
            emp.setIsActive(true);
            return repo.save(emp);
        });
    }

    public Employer updateEmployer(Long id, EmployerCreatedto dto) {
        Employer emp = repo.findById(id).orElse(null);

    if(emp != null) {
        emp.setContactFirstName(dto.getContactFirstName());
        emp.setContactLastName(dto.getContactLastName());
        emp.setCompanyName(dto.getCompanyName());
        emp.setIndustry(dto.getIndustry());
        emp.setWebsite(dto.getWebsite());

        return repo.save(emp);
    }

    return null;
    }

    @Transactional
    public Employer updateEmployerByEmail(String email, EmployerCreatedto dto) {
        Employer emp = repo.findByUserEmail(email)
                .orElseThrow(() -> new com.example.demo.exception.ResourceNotFoundException("Employer not found"));
        
        if (dto.getContactFirstName() != null && !dto.getContactFirstName().trim().isEmpty()) {
            emp.setContactFirstName(dto.getContactFirstName());
        }
        if (dto.getContactLastName() != null && !dto.getContactLastName().trim().isEmpty()) {
            emp.setContactLastName(dto.getContactLastName());
        }
        if (dto.getCompanyName() != null && !dto.getCompanyName().trim().isEmpty()) {
            emp.setCompanyName(dto.getCompanyName());
        }
        if (dto.getIndustry() != null) {
            emp.setIndustry(dto.getIndustry());
        }
        if (dto.getWebsite() != null) {
            emp.setWebsite(dto.getWebsite());
        }
        if (dto.getDescription() != null) {
            emp.setDescription(dto.getDescription());
        }
        if (dto.getPhone() != null) {
            emp.setPhone(dto.getPhone());
        }
        if (dto.getLocation() != null) {
            emp.setLocation(dto.getLocation());
        }
        
        return repo.save(emp);
    }

    public String deactivateEmployer(Long id) {

        Employer emp = repo.findById(id).orElse(null);

        if(emp != null) {
            emp.setIsActive(false);
            repo.save(emp);
            return "Employer Deactivated";
        }

        return "Employer Not Found";
    }

    public String reactivateEmployer(Long id) {

        Employer emp = repo.findById(id).orElse(null);

        if(emp != null) {
            emp.setIsActive(true);
            repo.save(emp);
            return "Employer Reactivated Successfully";
        }

        return "Employer Not Found";
    }
}
