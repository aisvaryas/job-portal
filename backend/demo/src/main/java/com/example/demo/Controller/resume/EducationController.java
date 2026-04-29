package com.example.demo.Controller.resume;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entity.resume.Education;
import com.example.demo.response.Apiresponse;
import com.example.demo.dto.resume.EducationRequest;
import com.example.demo.Service.resume.EducationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/education")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService service;

    @PostMapping("/save/{email}")
    public ResponseEntity<Apiresponse<String>> save(@PathVariable String email,@RequestBody EducationRequest request) {
        String msg = service.save(email, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Apiresponse<>("Education Saved Successfully",201,msg));
    }

    @GetMapping("/all/{email}")
    public ResponseEntity<Apiresponse<List<Education>>> getAll(@PathVariable String email) {
        return ResponseEntity.ok(
                new Apiresponse<>("Education Fetched Successfully",200,service.getAll(email)));
    }

    @PutMapping("/update/{email}/{id}")
    public ResponseEntity<Apiresponse<String>> update(@PathVariable String email,@PathVariable Long id,
            @RequestBody EducationRequest request) {
        String msg = service.update(email, id, request);
        return ResponseEntity.ok(
                new Apiresponse<>("Education Updated Successfully",200,msg));
    }

    @DeleteMapping("/delete/{email}/{id}")
    public ResponseEntity<Apiresponse<String>> delete(@PathVariable String email,@PathVariable Long id) {
        return ResponseEntity.ok(
                new Apiresponse<>(service.delete(email,id),200,"Success"));
    }
}