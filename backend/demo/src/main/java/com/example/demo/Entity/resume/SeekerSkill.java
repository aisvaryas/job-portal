package com.example.demo.Entity.resume;
import jakarta.persistence.*;
import lombok.*;

import com.example.demo.Entity.Jobseeker;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeekerSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seeker_id", nullable = false)
    private Jobseeker jobseeker;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;
    
}
