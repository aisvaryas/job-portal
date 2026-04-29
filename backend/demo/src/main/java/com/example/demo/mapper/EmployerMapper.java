package com.example.demo.mapper;
import com.example.demo.Entity.Employer;
import com.example.demo.Entity.User;
import com.example.demo.dto.EmployerCreatedto;
import com.example.demo.dto.EmployerResponsedto;

public class EmployerMapper {

    public static Employer toEntity(EmployerCreatedto dto, User user) {

        Employer emp = new Employer();

        emp.setUser(user);
        emp.setContactFirstName(dto.getContactFirstName());
        emp.setContactLastName(dto.getContactLastName());
        emp.setCompanyName(dto.getCompanyName());
        emp.setIndustry(dto.getIndustry());
        emp.setWebsite(dto.getWebsite());
        emp.setDescription(dto.getDescription());
        emp.setPhone(dto.getPhone());
        emp.setLocation(dto.getLocation());
        emp.setVerifiedStatus("PENDING");
        emp.setIsActive(true);

        return emp;
    }
    public static EmployerResponsedto toDto(Employer emp) {

    return new EmployerResponsedto(
            emp.getId(),
            emp.getContactFirstName(),
            emp.getContactLastName(),
            emp.getCompanyName(),
            emp.getIndustry(),
            emp.getWebsite(),
            emp.getDescription(),
            emp.getPhone(),
            emp.getLocation(),
            emp.getVerifiedStatus(),
            emp.getIsActive()
    );
}
}