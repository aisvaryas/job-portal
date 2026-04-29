package com.example.demo.mapper;

import com.example.demo.Entity.Jobseeker;
import com.example.demo.Entity.User;
import com.example.demo.dto.JobSeekerCreatedto;
import com.example.demo.dto.JobSeekerResponsedto;

public class JobseekerMapper {

    public static Jobseeker toEntity(JobSeekerCreatedto dto, User user) {

        Jobseeker seeker = new Jobseeker();

        seeker.setUser(user);
        seeker.setFirstName(dto.getFirstName());
        seeker.setLastName(dto.getLastName());
        seeker.setPhone(dto.getPhone());
        seeker.setLocation(dto.getLocation());
        seeker.setHeadline(dto.getHeadline());
        seeker.setSummary(dto.getSummary());
        seeker.setActive(true);

        return seeker;
    }
    public static JobSeekerResponsedto toDto(Jobseeker seeker) {

        return new JobSeekerResponsedto(
                seeker.getId(),
                seeker.getFirstName(),
                seeker.getLastName(),
                seeker.getPhone(),
                seeker.getLocation(),
                seeker.getHeadline(),
                seeker.getSummary(),
                seeker.isActive()
        );
    }
}
