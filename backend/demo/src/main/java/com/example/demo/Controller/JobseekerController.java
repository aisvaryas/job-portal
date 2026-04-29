package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.JobListing;
import com.example.demo.Entity.Jobseeker;
import com.example.demo.Service.JobListingService;
import com.example.demo.Service.JobseekerService;
import com.example.demo.dto.JobListingResponsedto;
import com.example.demo.dto.JobSeekerCreatedto;
import com.example.demo.dto.JobSeekerProfiledto;
import com.example.demo.dto.JobSeekerResponsedto;
import com.example.demo.mapper.JobListingMapper;
import com.example.demo.mapper.JobseekerMapper;
import com.example.demo.response.Apiresponse;

@RestController
@RequestMapping("/api/jobseeker")
public class JobseekerController {

    @Autowired
    private JobseekerService service;

    @Autowired
    private JobListingService jobService;

    @PostMapping("/save")
    public ResponseEntity<Apiresponse<JobSeekerResponsedto>> save(
        @RequestBody JobSeekerCreatedto dto) {

        Jobseeker seeker = service.saveJobseeker(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new Apiresponse<>(
                "Jobseeker Saved Successfully",
                201,
                JobseekerMapper.toDto(seeker)
            ));
    }

    @GetMapping("/all")
    public ResponseEntity<Apiresponse<List<Jobseeker>>> all() {
        List<Jobseeker> list = service.getAllJobseekers();
        if(list.isEmpty()) throw new com.example.demo.exception.ResourceNotFoundException("No Jobseekers Found");
        return ResponseEntity.ok(new Apiresponse<>("Jobseekers Fetched Successfully",200,list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Apiresponse<JobSeekerResponsedto>> byId(@PathVariable Long id) {
        Jobseeker seeker = service.getJobseekerById(id);
        if(seeker == null)
            throw new com.example.demo.exception.ResourceNotFoundException("Jobseeker Not Found");
        return ResponseEntity.ok(
                new Apiresponse<>("Jobseeker Found",200,JobseekerMapper.toDto(seeker)));
    }

    @GetMapping("/profile/{email}")
    public ResponseEntity<Apiresponse<JobSeekerProfiledto>> getProfile(@PathVariable String email) {
        Jobseeker seeker = service.getJobseekerByEmail(email);
        if(seeker == null)
            throw new com.example.demo.exception.ResourceNotFoundException("Jobseeker Not Found");
        
        JobSeekerProfiledto profile = toProfileDto(seeker);
        
        return ResponseEntity.ok(
                new Apiresponse<>("Profile Fetched Successfully",200,profile));
    }

    @GetMapping("/profile")
    public ResponseEntity<Apiresponse<JobSeekerProfiledto>> getCurrentProfile(Authentication authentication) {
        String email = authentication.getName();
        Jobseeker seeker = service.ensureJobseekerByEmail(email);

        return ResponseEntity.ok(
                new Apiresponse<>("Profile Fetched Successfully",200,toProfileDto(seeker)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Apiresponse<JobSeekerProfiledto>> update(@PathVariable Long id,@RequestBody JobSeekerCreatedto dto) {
        Jobseeker seeker = service.updateJobseeker(id,dto);
        if(seeker==null) throw new com.example.demo.exception.ResourceNotFoundException("Jobseeker Not Found");
        return ResponseEntity.ok(new Apiresponse<>("Jobseeker Updated Successfully",200,toProfileDto(seeker)));
    }

    @PutMapping("/profile")
    public ResponseEntity<Apiresponse<JobSeekerProfiledto>> updateCurrentProfile(
            Authentication authentication,
            @RequestBody JobSeekerCreatedto dto) {
        Jobseeker seeker = service.updateCurrentJobseeker(authentication.getName(), dto);
        return ResponseEntity.ok(new Apiresponse<>("Profile Updated Successfully",200,toProfileDto(seeker)));
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<Apiresponse<String>> deactivate(@PathVariable Long id) {
        String msg = service.deactivateJobseeker(id);
        return ResponseEntity.ok(new Apiresponse<>(msg,200,msg));
    }

    @PutMapping("/reactivate/{id}")
    public ResponseEntity<Apiresponse<String>> reactivate(@PathVariable Long id) {
        String msg = service.reactivateJobseeker(id);
        return ResponseEntity.ok(new Apiresponse<>(msg,200,msg));
    }

    //job seekers access for job listing

    @GetMapping("/jobs/all")
    public ResponseEntity<Apiresponse<List<JobListingResponsedto>>> allJobs() {
        List<JobListing> list = jobService.getAll();
        if(list.isEmpty()) {
            throw new com.example.demo.exception.ResourceNotFoundException("No Jobs Found");
        }
        List<JobListingResponsedto> dtoList =
                list.stream()
                    .map(JobListingMapper::toDto)
                    .toList();
        return ResponseEntity.ok(
                new Apiresponse<>("Jobs Fetched Successfully",200,dtoList));
    }

    @GetMapping("/jobs/{jobId}")
    public ResponseEntity<Apiresponse<JobListingResponsedto>> jobById(@PathVariable Long jobId) {
        JobListing job = jobService.getById(jobId);
        if(job == null)
            throw new com.example.demo.exception.ResourceNotFoundException("Job Not Found");
        return ResponseEntity.ok(
                new Apiresponse<>("Job Found",200,JobListingMapper.toDto(job)));
    }

    @GetMapping("/jobs/search")
    public ResponseEntity<Apiresponse<List<JobListingResponsedto>>> searchJobs(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String experience) {

        List<JobListing> list = jobService.searchOpenJobs(title, location, experience);

        if (list.isEmpty()) {
            throw new com.example.demo.exception.ResourceNotFoundException("No Jobs Found");
        }

        List<JobListingResponsedto> dtoList =
                list.stream()
                    .map(JobListingMapper::toDto)
                    .toList();

        return ResponseEntity.ok(new Apiresponse<>("Jobs Found Successfully",200,dtoList));
    }

    private JobSeekerProfiledto toProfileDto(Jobseeker seeker) {
        return new JobSeekerProfiledto(
                seeker.getId(),
                seeker.getFirstName(),
                seeker.getLastName(),
                seeker.getUser().getEmail(),
                seeker.getPhone(),
                seeker.getLocation(),
                seeker.getHeadline(),
                seeker.getSummary(),
                seeker.isActive()
        );
    }
}
