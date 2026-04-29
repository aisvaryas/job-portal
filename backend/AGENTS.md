# AI Agent Guide: CareerCrafter

CareerCrafter is a **Spring Boot REST API** for a job marketplace platform connecting employers and job seekers.

## Quick Start

**Build & Test**
```bash
cd demo
mvn clean install      # Build the project
mvn test               # Run tests
mvn spring-boot:run    # Start the application (port 8084)
```

**Database Setup**
- MySQL 8.0 running on `localhost:3306`
- Database: `cc` (credentials in [application.properties](demo/src/main/resources/application.properties))
- Hibernate auto-creates/updates schema (`ddl-auto=update`)

## Architecture

### Three-Tier Pattern
All modules follow this structure:
- **Controllers** (`Controller/`): Handle HTTP requests, return wrapped responses
- **Services** (`Service/`): Business logic, data transformation
- **Repositories** (`Repository/`): Data access (Spring Data JPA)
- **DTOs** (`dto/`): Request/response contracts
- **Entities** (`Entity/`): JPA persistence models

### Core Entities

**User** (base)
- Roles: `JOB_SEEKER` | `EMPLOYER`
- Soft-delete pattern: `isActive` flag instead of hard delete
- Email unique, password stored, createdAt auto-timestamp

**Employer** (OneToOne with User)
- Contact name, company name, industry, website
- Verification status field

**Jobseeker** (OneToOne with User)
- Personal profile: firstName, lastName, phone (10-digit validation), location, headline
- isActive flag

**JobListing** (OneToMany from Employer)
- Job title, description, requirements, salary range, location
- Posted by employer

**Role** (Enum)
- `JOB_SEEKER`, `EMPLOYER`

### Response Wrapper

All endpoints return `Apiresponse<T>`:
```java
{
  "message": "success/error message",
  "status": 200,
  "timestamp": "2025-04-25T10:30:00",
  "data": {...}
}
```

**When to use**: Wrap every response using `new Apiresponse<>(message, statusCode, data)`

## Common Patterns

### Exception Handling
Try-catch at controller level, return appropriate HTTP status + error message:
```java
try {
    // business logic
    return ResponseEntity.ok(new Apiresponse<>("Success", 200, result));
} catch (Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new Apiresponse<>("Error message", 500, null));
}
```

### Validation
Use Jakarta validation annotations on DTOs:
- `@NotBlank`, `@Pattern`, `@Email`, etc.
- Validation message: `message="error text"`

### Endpoints
Pattern: `/{entity}/{action}`
- GET `/users/all` - list all users
- GET `/users/{id}` - get by ID
- POST `/users/register` - create user
- PUT `/users/deactivate/{id}` - soft delete user
- Similar pattern for employers, jobseekers, joblistings

## File Conventions

- **Entities** end with `.java` (e.g., `User.java`, `Employer.java`)
- **DTOs** follow pattern `{Entity}Createdto.java` or `{Entity}Updatedto.java`
- **Services** follow pattern `{Entity}Service.java`
- **Repositories** follow pattern `{Entity}Repo.java`
- **Controllers** follow pattern `{Entity}Controller.java`

## Complete DTO Inventory

| DTO | Purpose | Used By |
|-----|---------|---------|
| `UserRegisterdto` | Register new user (email, password, role) | UserService.saveUser() |
| `UserUpdatedto` | Update user credentials | UserService.updateUser() |
| `UserResponsedto` | User response (id, email, role, createdAt, isActive) | User queries |
| `Logindto` | Login authentication (email, password) | Authentication |
| `EmployerCreatedto` | Create employer profile (contact name, company, industry, website) | EmployerService.saveEmployer() |
| `JobSeekerCreatedto` | Create jobseeker profile (firstName, lastName, phone, location, headline) | JobseekerService.saveJobseeker() |
| `JobCreatedto` | Create job listing (title, description, location, jobType, salaryMin, salaryMax) | JobListingService.save() |

## Complete Service Inventory

| Service | Key Methods | Entities Handled |
|---------|------------|------------------|
| `UserService` | saveUser(), getAllUsers(), getUserById(), updateUser(), deleteUser() (soft-delete) | User |
| `EmployerService` | saveEmployer(), getAllEmployers(), getEmployerById(), updateEmployer(), deactivateEmployer() | Employer |
| `JobseekerService` | saveJobseeker(), getAllJobseekers(), getJobseekerById(), updateJobseeker(), deactivateJobseeker() | Jobseeker |
| `JobListingService` | save(), getAll(), getById(), updateJob(), deleteJob() | JobListing |

## Development Tips

1. **When adding an entity**: Create DTO, Service, Repository, and Controller together
2. **When modifying APIs**: Remember soft-delete pattern (set `isActive=false`, don't remove records)
3. **Database changes**: Hibernate will auto-update schema, but verify `ddl-auto=update` is set
4. **Testing**: Check [DemoApplicationTests.java](demo/src/test/java/com/example/demo/DemoApplicationTests.java)
5. **Error responses**: Always include descriptive message and appropriate HTTP status code

## Key Files

**Configuration**
- [pom.xml](demo/pom.xml) - Maven dependencies and build config
- [application.properties](demo/src/main/resources/application.properties) - Database and server config

**Entities**
- [User.java](demo/src/main/java/com/example/demo/Entity/User.java) - Base user entity
- [Employer.java](demo/src/main/java/com/example/demo/Entity/Employer.java) - Employer profile
- [Jobseeker.java](demo/src/main/java/com/example/demo/Entity/Jobseeker.java) - Jobseeker profile
- [JobListing.java](demo/src/main/java/com/example/demo/Entity/JobListing.java) - Job listings
- [Role.java](demo/src/main/java/com/example/demo/Entity/Role.java) - Role enum (JOB_SEEKER, EMPLOYER)

**DTOs**
- [UserRegisterdto.java](demo/src/main/java/com/example/demo/dto/UserRegisterdto.java)
- [UserUpdatedto.java](demo/src/main/java/com/example/demo/dto/UserUpdatedto.java)
- [UserResponsedto.java](demo/src/main/java/com/example/demo/dto/UserResponsedto.java)
- [Logindto.java](demo/src/main/java/com/example/demo/dto/Logindto.java)
- [EmployerCreatedto.java](demo/src/main/java/com/example/demo/dto/EmployerCreatedto.java)
- [JobSeekerCreatedto.java](demo/src/main/java/com/example/demo/dto/JobSeekerCreatedto.java)
- [JobCreatedto.java](demo/src/main/java/com/example/demo/dto/JobCreatedto.java)

**Services & Controllers**
- [UserService.java](demo/src/main/java/com/example/demo/Service/UserService.java) - User business logic
- [EmployerService.java](demo/src/main/java/com/example/demo/Service/EmployerService.java) - Employer business logic
- [JobseekerService.java](demo/src/main/java/com/example/demo/Service/JobseekerService.java) - Jobseeker business logic
- [JobListingService.java](demo/src/main/java/com/example/demo/Service/JobListingService.java) - Job listing business logic
- [UserController.java](demo/src/main/java/com/example/demo/Controller/UserController.java) - User API endpoints
- [EmployerController.java](demo/src/main/java/com/example/demo/Controller/EmployerController.java) - Employer API endpoints
- [JobseekerController.java](demo/src/main/java/com/example/demo/Controller/JobseekerController.java) - Jobseeker API endpoints
- [JobListingController.java](demo/src/main/java/com/example/demo/Controller/JobListingController.java) - Job listing API endpoints

**Response Wrapper**
- [Apiresponse.java](demo/src/main/java/com/example/demo/response/Apiresponse.java) - Standard response wrapper

## Tech Stack

- **Framework**: Spring Boot 3.5.14
- **Language**: Java 21
- **Database**: MySQL 8.0 + Hibernate JPA
- **Validation**: Jakarta Validation
- **Build**: Maven
- **View Templates**: JSP + JSTL (if needed)
