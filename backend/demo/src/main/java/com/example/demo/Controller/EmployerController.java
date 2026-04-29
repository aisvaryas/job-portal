package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Employer;
import com.example.demo.Entity.JobListing;
import com.example.demo.Entity.JobApplication;
import com.example.demo.Service.EmployerService;
import com.example.demo.Service.JobListingService;
import com.example.demo.Service.JobApplicationService;
import com.example.demo.dto.EmployerCreatedto;
import com.example.demo.dto.EmployerResponsedto;
import com.example.demo.dto.ApplicationResponsedto;
import com.example.demo.dto.JobCreatedto;
import com.example.demo.dto.JobListingResponsedto;
import com.example.demo.mapper.EmployerMapper;
import com.example.demo.mapper.JobListingMapper;
import com.example.demo.response.Apiresponse;

@RestController
@RequestMapping("/api/employer")
public class EmployerController {

    @Autowired
    private EmployerService service;

    @Autowired
    private JobListingService jobService;
    
    @Autowired
    private JobApplicationService applicationService;

    @PostMapping("/save")
    public ResponseEntity<Apiresponse<EmployerResponsedto>> save(@RequestBody EmployerCreatedto dto) {
        Employer emp = service.saveEmployer(dto);
        EmployerResponsedto resDto = EmployerMapper.toDto(emp);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Apiresponse<>("Employer Saved Successfully",201,resDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Apiresponse<EmployerResponsedto>> byId(@PathVariable Long id) {
        Employer emp = service.getEmployerById(id);
        if(emp == null)
            throw new com.example.demo.exception.ResourceNotFoundException("Employer Not Found");
        return ResponseEntity.ok(
                new Apiresponse<>("Employer Found",200,EmployerMapper.toDto(emp)));
    }

    @GetMapping("/profile")
    public ResponseEntity<Apiresponse<EmployerResponsedto>> currentProfile(Authentication authentication) {
        Employer emp = service.ensureEmployerByEmail(authentication.getName());
        return ResponseEntity.ok(
                new Apiresponse<>("Employer Found",200,EmployerMapper.toDto(emp)));
    }

    @PutMapping("/profile")
    public ResponseEntity<Apiresponse<EmployerResponsedto>> updateProfile(Authentication authentication, @RequestBody EmployerCreatedto dto) {
        Employer emp = service.updateEmployerByEmail(authentication.getName(), dto);
        return ResponseEntity.ok(
                new Apiresponse<>("Employer Updated Successfully",200,EmployerMapper.toDto(emp)));
    }

    @GetMapping("/all")
    public ResponseEntity<Apiresponse<List<Employer>>> all() {
        List<Employer> list = service.getAllEmployers();
        if(list.isEmpty()) throw new com.example.demo.exception.ResourceNotFoundException("No Employers Found");
        return ResponseEntity.ok(new Apiresponse<>("Employers Fetched Successfully",200,list));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Apiresponse<Employer>> update(@PathVariable Long id,@RequestBody EmployerCreatedto dto) {
        Employer emp = service.updateEmployer(id,dto);
        if(emp == null)
            throw new com.example.demo.exception.ResourceNotFoundException("Employer Not Found");
        return ResponseEntity.ok(new Apiresponse<>("Employer Updated Successfully",200,emp));
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<Apiresponse<String>> deactivate(@PathVariable Long id) {
        String msg = service.deactivateEmployer(id);
        return ResponseEntity.ok(new Apiresponse<>(msg,200,msg));
    }

    @PutMapping("/reactivate/{id}")
    public ResponseEntity<Apiresponse<String>> reactivate(@PathVariable Long id) {
        String msg = service.reactivateEmployer(id);
        return ResponseEntity.ok(new Apiresponse<>(msg,200,msg));
    }

    // job listing for employer
    @PostMapping("/{id}/jobs")
    public ResponseEntity<Apiresponse<JobListing>> postJob(@PathVariable Long id,@RequestBody JobCreatedto dto) {
        Employer emp = service.getEmployerById(id);
        if(emp==null) throw new com.example.demo.exception.ResourceNotFoundException("Employer Not Found");
        JobListing job = jobService.save(dto,emp);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Apiresponse<>("Job Posted Successfully",201,job));
    }

    @PutMapping("/{id}/jobs/{jobId}")
    public ResponseEntity<Apiresponse<JobListing>> updateJob(@PathVariable Long id,@PathVariable Long jobId,@RequestBody JobCreatedto dto) {
        Employer emp = service.getEmployerById(id);
        if(emp==null) throw new com.example.demo.exception.ResourceNotFoundException("Employer Not Found");
        JobListing job = jobService.updateJob(jobId,dto);
        return ResponseEntity.ok(new Apiresponse<>("Job Updated Successfully",200,job));
    }

    @PutMapping("/jobs/close/{jobId}")
    public ResponseEntity<Apiresponse<String>> closeJob(@PathVariable Long jobId) {
        String msg = jobService.closeJob(jobId);
        return ResponseEntity.ok(new Apiresponse<>(msg,200,msg));
    }
    
    @PutMapping("/jobs/reopen/{jobId}")
    public ResponseEntity<Apiresponse<String>> reopenJob(@PathVariable Long jobId) {
        String msg = jobService.reopenJob(jobId);
        return ResponseEntity.ok(new Apiresponse<>(msg,200,msg));
    }
    
    @DeleteMapping("/jobs/delete/{jobId}")
    public ResponseEntity<Apiresponse<String>> deleteJob(@PathVariable Long jobId) {
        String msg = jobService.deleteJob(jobId);
        return ResponseEntity.ok(new Apiresponse<>(msg,200,msg));
    }

    @GetMapping("/{id}/jobs")
    public ResponseEntity<Apiresponse<List<JobListingResponsedto>>> employerJobs(@PathVariable Long id) {
        List<JobListing> list = jobService.getJobsByEmployer(id);
        List<JobListingResponsedto> dtoList = list.stream()
                .map(job -> {
                    JobListingResponsedto dto = JobListingMapper.toDto(job);
                    dto.setApplicantCount(applicationService.getApplicationCountByJob(job.getId()));
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(new Apiresponse<>("Employer Jobs Fetched Successfully",200,dtoList));
    }
    
    // Applicant Management
    @GetMapping("/jobs/{jobId}/applicants")
    public ResponseEntity<Apiresponse<List<ApplicationResponsedto>>> getJobApplicants(@PathVariable Long jobId) {
        List<ApplicationResponsedto> applications = applicationService.getApplicationDtosByJob(jobId);
        return ResponseEntity.ok(new Apiresponse<>("Applicants Fetched Successfully",200,applications));
    }
    
    @GetMapping("/jobs/{jobId}/applicants/count")
    public ResponseEntity<Apiresponse<Long>> getApplicationCount(@PathVariable Long jobId) {
        long count = applicationService.getApplicationCountByJob(jobId);
        return ResponseEntity.ok(new Apiresponse<>("Application Count",200,count));
    }
    
    @PutMapping("/applications/{applicationId}/status")
    public ResponseEntity<Apiresponse<ApplicationResponsedto>> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam String status) {
        ApplicationResponsedto application = applicationService.updateApplicationStatusDto(applicationId, status);
        return ResponseEntity.ok(new Apiresponse<>("Application Status Updated Successfully",200,application));
    }
}
