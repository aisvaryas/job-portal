package com.example.demo.Controller.resume;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entity.resume.SeekerSkill;
import com.example.demo.Service.resume.SeekerSkillService;
import com.example.demo.dto.resume.SkillRequest;
import com.example.demo.dto.resume.SkillResponse;
import com.example.demo.mapper.SkillMapper;
import com.example.demo.response.Apiresponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/seekerskill")
@RequiredArgsConstructor
public class SeekerSkillController {

    private final SeekerSkillService service;

    @PostMapping("/save/{email}")
    public ResponseEntity<Apiresponse<SkillResponse>> addSkill(
            @PathVariable String email,
            @RequestBody SkillRequest request) {

        SeekerSkill seekerSkill = service.addSkill(email, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Apiresponse<>("Skill Added Successfully", 201, SkillMapper.toSimpleResponse(seekerSkill)));
    }

    @GetMapping("/all/{email}")
    public ResponseEntity<Apiresponse<List<SkillResponse>>> getAllSkills(
            @PathVariable String email) {

        List<SeekerSkill> list = service.getAllSkills(email);
        List<SkillResponse> responseList = list.stream()
                .map(SkillMapper::toSimpleResponse)
                .toList();

        return ResponseEntity.ok(new Apiresponse<>("Skills Fetched Successfully", 200, responseList));
    }

    @PutMapping("/update/{email}/{oldSkill}")
    public ResponseEntity<Apiresponse<SkillResponse>> updateSkill(@PathVariable String email,@PathVariable String oldSkill,@RequestBody SkillRequest request) {

        SeekerSkill seekerSkill = service.updateSkill(email, oldSkill, request);
        return ResponseEntity.ok(new Apiresponse<>("Skill Updated Successfully", 200, SkillMapper.toSimpleResponse(seekerSkill)));
    }

    @DeleteMapping("/delete/{email}/{skillName}")
    public ResponseEntity<Apiresponse<String>> deleteSkill(@PathVariable String email,@PathVariable String skillName) {
        String msg = service.deleteSkill(email, skillName);
        return ResponseEntity.ok(new Apiresponse<>(msg, 200, msg));
    }
}