package com.example.demo.Controller.resume;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entity.resume.Skill;
import com.example.demo.Service.resume.SkillService;
import com.example.demo.dto.resume.SkillRequest;
import com.example.demo.response.Apiresponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/skill")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService service;
    @PostMapping("/save")
    public ResponseEntity<Apiresponse<Skill>> save(@RequestBody SkillRequest request) {
        Skill skill = service.addSkill(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Apiresponse<>("Skill Saved Successfully", 201, skill));
    }

    @GetMapping("/all")
    public ResponseEntity<Apiresponse<List<Skill>>> getAll() {
        List<Skill> list = service.getAllSkills();
        return ResponseEntity.ok(new Apiresponse<>("Skills Fetched Successfully", 200, list));
    }
}