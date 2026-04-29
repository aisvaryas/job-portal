package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entity.Jobseeker;
import com.example.demo.Entity.User;
import com.example.demo.Repository.JobSeekerRepo;
import com.example.demo.Repository.UserRepo;
import com.example.demo.dto.JobSeekerCreatedto;
import com.example.demo.mapper.JobseekerMapper;

@Service
public class JobseekerService {

    @Autowired
    private JobSeekerRepo repo;
    @Autowired
    private UserRepo userRepo;

    @Transactional
    public Jobseeker saveJobseeker(JobSeekerCreatedto dto) {
        User user = userRepo.findById(dto.getUserId()).orElse(null);

        if(user == null) {
            throw new RuntimeException("User not found with id: " + dto.getUserId());
        }
    Jobseeker seeker = JobseekerMapper.toEntity(dto,user);
        
    return repo.save(seeker);
    }

    @Transactional
    public Jobseeker ensureJobseekerExists(User user) {
        // Check if jobseeker already exists for this user
        Optional<Jobseeker> existing = repo.findByUser(user);
        if (existing.isPresent()) {
            return existing.get();
        }

        // Create a new jobseeker if not exists
        String[] nameParts = (user.getEmail() != null ? user.getEmail().split("@")[0] : "Seeker").split("[._-]+");
        String firstName = nameParts.length > 0 ? nameParts[0] : "Seeker";
        String lastName = nameParts.length > 1 ? String.join(" ", java.util.Arrays.copyOfRange(nameParts, 1, nameParts.length)) : "User";

        Jobseeker newSeeker = new Jobseeker();
        newSeeker.setUser(user);
        newSeeker.setFirstName(firstName.substring(0, 1).toUpperCase() + firstName.substring(1));
        newSeeker.setLastName(lastName);
        newSeeker.setPhone(null); // Phone is optional initially
        newSeeker.setLocation("");
        newSeeker.setHeadline("");
        newSeeker.setSummary("");
        newSeeker.setActive(true);

        return repo.save(newSeeker);
    }

    public List<Jobseeker> getAllJobseekers() {
        return repo.findAll();
    }

    public Jobseeker getJobseekerById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    public Jobseeker updateJobseeker(Long id, JobSeekerCreatedto dto) {

    Jobseeker seeker = repo.findById(id).orElse(null);

    if(seeker != null) {
        applyProfileUpdate(seeker, dto);

        return repo.save(seeker);
    }

    return null;
    }

    @Transactional
    public String deactivateJobseeker(Long id) {

        Jobseeker seeker = repo.findById(id).orElse(null);

        if (seeker != null) {
            seeker.setActive(false);
            repo.save(seeker);
            return "Jobseeker DEactivated Successfully";
        }
        

        return "Jobseeker Not Found";
    }

    @Transactional
    public String reactivateJobseeker(Long id) {

        Jobseeker seeker = repo.findById(id).orElse(null);

        if(seeker != null) {
            seeker.setActive(true);
            repo.save(seeker);
            return "Jobseeker Reactivated Successfully";
        }

        return "Jobseeker Not Found";
    }

    public Jobseeker getJobseekerByEmail(String email) {
        return repo.findByUserEmail(email).orElse(null);
    }

    public Jobseeker ensureJobseekerByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new com.example.demo.exception.ResourceNotFoundException("User not found"));
        return ensureJobseekerExists(user);
    }

    @Transactional
    public Jobseeker updateCurrentJobseeker(String email, JobSeekerCreatedto dto) {
        Jobseeker seeker = ensureJobseekerByEmail(email);
        applyProfileUpdate(seeker, dto);
        return repo.save(seeker);
    }

    private void applyProfileUpdate(Jobseeker seeker, JobSeekerCreatedto dto) {
        String firstName = normalizeText(dto.getFirstName());
        String lastName = normalizeText(dto.getLastName());

        seeker.setFirstName(firstName == null ? seeker.getFirstName() : firstName);
        seeker.setLastName(lastName == null ? seeker.getLastName() : lastName);
        seeker.setPhone(normalizePhone(dto.getPhone()));
        seeker.setLocation(normalizeText(dto.getLocation()));
        seeker.setHeadline(normalizeText(dto.getHeadline()));
        seeker.setSummary(normalizeText(dto.getSummary()));
    }

    private String normalizeText(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    private String normalizePhone(String value) {
        String phone = normalizeText(value);
        if (phone != null && !phone.matches("^[0-9]{10}$")) {
            throw new com.example.demo.exception.BusinessException("Enter valid 10 digit phone number");
        }
        return phone == null ? null : phone;
    }
}
