package com.example.demo.Entity.resume;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.example.demo.Entity.Jobseeker;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seeker_id", nullable = false)
    private Jobseeker jobseeker;

    @Column(nullable = false)
    private String institution;

    @Column(nullable = false)
    private String degree;


    private String fieldOfStudy;

    private String description;

    @Column(nullable = false)
    private LocalDate graduationDate;

    private String grade;
}