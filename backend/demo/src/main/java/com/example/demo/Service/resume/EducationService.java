package com.example.demo.Service.resume;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.example.demo.dto.resume.EducationRequest;

import lombok.RequiredArgsConstructor;

import com.example.demo.Entity.Jobseeker;
import com.example.demo.Entity.resume.Education;
import com.example.demo.Repository.JobSeekerRepo;
import com.example.demo.Repository.resume.EducationRepo;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class EducationService {

    
    private final EducationRepo repo;
    private final JobSeekerRepo jobSeekerRepo;

    @Transactional
    public String save(String email, EducationRequest dto) {
        // Validate input
        if (dto == null || dto.getInstitution() == null || dto.getInstitution().trim().isEmpty()) {
            throw new BusinessException("Institution name is required");
        }
        if (dto.getDegree() == null || dto.getDegree().trim().isEmpty()) {
            throw new BusinessException("Degree is required");
        }
        if (dto.getGraduationDate() == null) {
            throw new BusinessException("Graduation date is required");
        }

        Jobseeker seeker = jobSeekerRepo.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found with email: " + email));

        Education edu = new Education();
        edu.setJobseeker(seeker);
        edu.setInstitution(dto.getInstitution().trim());
        edu.setDegree(dto.getDegree().trim());
        edu.setFieldOfStudy(dto.getFieldOfStudy() != null ? dto.getFieldOfStudy().trim() : null);
        edu.setDescription(dto.getDescription());
        edu.setGraduationDate(dto.getGraduationDate());
        edu.setGrade(dto.getGrade() != null ? dto.getGrade().trim() : null);

        repo.save(edu);
        return "Education Saved Successfully";
    }

    public List<Education> getAll(String email) {
        Jobseeker seeker = jobSeekerRepo.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found with email: " + email));
        return repo.findByJobseeker(seeker);
    }

    @Transactional
    public String update(String email, Long id, EducationRequest dto) {
        // Validate input
        if (dto == null || dto.getInstitution() == null || dto.getInstitution().trim().isEmpty()) {
            throw new BusinessException("Institution name is required");
        }
        if (dto.getDegree() == null || dto.getDegree().trim().isEmpty()) {
            throw new BusinessException("Degree is required");
        }
        if (dto.getGraduationDate() == null) {
            throw new BusinessException("Graduation date is required");
        }

        Jobseeker seeker = jobSeekerRepo.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found with email: " + email));

        Education edu = repo.findByIdAndJobseeker(id, seeker)
                .orElseThrow(() -> new ResourceNotFoundException("Education record not found with id: " + id));

        edu.setInstitution(dto.getInstitution().trim());
        edu.setDegree(dto.getDegree().trim());
        edu.setFieldOfStudy(dto.getFieldOfStudy() != null ? dto.getFieldOfStudy().trim() : null);
        edu.setDescription(dto.getDescription());
        edu.setGraduationDate(dto.getGraduationDate());
        edu.setGrade(dto.getGrade() != null ? dto.getGrade().trim() : null);

        repo.save(edu);
        return "Education Updated Successfully";
    }

    @Transactional
    public String delete(String email, Long id) {
        Jobseeker seeker = jobSeekerRepo.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found with email: " + email));

        Education edu = repo.findByIdAndJobseeker(id, seeker)
                .orElseThrow(() -> new ResourceNotFoundException("Education record not found with id: " + id));

        repo.deleteById(id);
        return "Education Deleted Successfully";
    }
}