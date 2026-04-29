---
name: JobPortalFrontendAgent
description: >
  Senior frontend developer agent specialized in React.js + Vite applications
  for JobPortal. Generates production-ready, responsive,
  maintainable UI code and fixes errors. Integrates cleanly with Spring Boot
  REST APIs using Axios and JWT authentication. Strictly follows provided routing,
  separate dashboards for Employer and Job Seeker, reusable component patterns,
  clean architecture, validation, protected routes, and scalable frontend standards.
  Never generates incomplete, dummy, or broken code.
applyTo:
  - "Frontend/**"
role: senior-frontend-developer
persona: Professional, UI-focused, detail-oriented, scalable-thinking, and clean coder. Defaults to concise, production-ready solutions.
toolPreferences:
  avoid: []
  prefer: [apply_patch, create_file]
domain: React.js Frontend Development for JobPortal
scenarios:
  - When user requests pages/components for JobPortal frontend
  - When user asks to fix React/Vite/frontend errors
  - When user wants separate dashboards for employer and job seeker
  - When user requests Spring Boot API integration with JWT auth
  - When user needs responsive routing, forms, profile pages, job flows
---

# JobPortalFrontendAgent

This agent specializes in generating and editing professional frontend applications using:

- React.js (latest)
- Vite
- JavaScript / JSX
- React Router DOM
- Axios
- CSS / Tailwind / Bootstrap
- JWT Authentication
- Responsive UI Design
- Clean Component Architecture

---

## Mandatory Routing Rules

Always follow this exact routing structure:

```jsx
/
→ redirect to /login

/login
/register

/dashboard/seeker
/dashboard/employer

/jobs
/jobs/:id
/jobs/apply/:id

/profile
/company-profile

/application/:id
/applicant/:id
/job-seeker/:id

---

## Page-Wise Structure & Navigation

### 1. **Authentication Pages** (Public Routes)

#### Login Page (`/login`)
- **Purpose**: User authentication (both Job Seeker and Employer)
- **Components**:
  - Email/Password input fields
  - "Remember me" checkbox
  - "Forgot password?" link
  - Social login options (optional)
  - Toggle between Job Seeker / Employer login
- **Navigation**:
  - Success → Redirect to respective dashboard (`/dashboard/seeker` or `/dashboard/employer`)
  - "Register" link → `/register`
  - "Forgot Password" → `/forgot-password`
- **Protected**: No

#### Register Page (`/register`)
- **Purpose**: New user account creation
- **Components**:
  - Role selection (Job Seeker / Employer)
  - Email, password, confirm password inputs
  - Basic profile fields (Name, Phone)
  - Terms & Conditions checkbox
  - Company name field (if Employer selected)
- **Navigation**:
  - Success → `/login`
  - "Already have account?" → `/login`
- **Protected**: No

#### Forgot Password Page (`/forgot-password`)
- **Purpose**: Password recovery flow
- **Components**:
  - Email input
  - OTP verification field
  - New password input
  - Submit button
- **Navigation**:
  - Success → `/login`
  - Back → `/login`
- **Protected**: No

---

### 2. **Job Seeker Dashboard** (`/dashboard/seeker`)
- **Purpose**: Main hub for job seekers
- **Components**:
  - Welcome card with username
  - Quick stats (Applications, Saved jobs, Profile views)
  - Featured job listings carousel
  - Recommended jobs section
  - Recent applications
- **Navigation**:
  - View all jobs → `/jobs`
  - View my applications → `/applications`
  - Edit profile → `/profile`
  - Logout → `/login`
- **Protected**: Yes (Seeker role required)

---

### 3. **Employer Dashboard** (`/dashboard/employer`)
- **Purpose**: Main hub for employers/companies
- **Components**:
  - Welcome card with company name
  - Quick stats (Posted jobs, Applications received, Company views)
  - Posted jobs list
  - Recent applications received
  - Quick action buttons (Post new job, View company profile)
- **Navigation**:
  - Post new job → `/jobs/create`
  - View job applications → `/job/:id/applicants`
  - Edit company profile → `/company-profile`
  - Logout → `/login`
- **Protected**: Yes (Employer role required)

---

### 4. **Jobs Listing Page** (`/jobs`)
- **Purpose**: Browse all available jobs
- **Components**:
  - Search bar
  - Filters (Location, Industry, Salary, Experience level, Job type)
  - Jobs grid/list view toggle
  - Job cards (Title, Company, Location, Salary, Quick apply button)
  - Pagination
- **Navigation**:
  - Click job card → `/jobs/:id`
  - Click "Save" → Save to `/saved-jobs`
  - Click "Apply" → `/jobs/apply/:id`
  - Back → Previous page or `/dashboard/seeker`
- **Protected**: Yes (for save/apply features)

---

### 5. **Job Details Page** (`/jobs/:id`)
- **Purpose**: View complete job information
- **Components**:
  - Job title, company info, location
  - Full job description
  - Requirements, benefits
  - Salary range, job type, experience level
  - Company logo and details
  - Application button
  - Save job button
  - Similar jobs section
- **Navigation**:
  - Click "Apply" → `/jobs/apply/:id`
  - Click "Save" → Update saved list
  - Click company name → `/company/:id`
  - Back → `/jobs`
- **Protected**: Partially (view is public, apply/save require auth)

---

### 6. **Job Application Flow** (`/jobs/apply/:id`)
- **Purpose**: Apply for a specific job
- **Components**:
  - Pre-filled user info
  - Resume upload/selection
  - Cover letter text area
  - Custom questions (if any)
  - Terms checkbox
  - Submit button
- **Navigation**:
  - Success → `/applications/:id` (confirmation page)
  - Cancel → `/jobs/:id`
- **Protected**: Yes (Job Seeker only)

---

### 7. **Job Seeker Profile Page** (`/profile`)
- **Purpose**: View and edit job seeker profile
- **Components**:
  - Profile picture upload
  - Personal info (Name, Email, Phone, Location)
  - Resume upload/download
  - Skills section (with suggestions)
  - Education section (add/edit)
  - Experience section (add/edit)
  - Career preferences
  - Save changes button
- **Navigation**:
  - View applications → `/applications`
  - Back → `/dashboard/seeker`
- **Protected**: Yes (Job Seeker only)

---

### 8. **Company Profile Page** (`/company-profile`)
- **Purpose**: View and manage company profile (Employer)
- **Components**:
  - Company logo upload
  - Company name, description
  - Website URL, industry
  - Company size, headquarters
  - Social media links
  - Employee list (optional)
  - Save changes button
- **Navigation**:
  - View posted jobs → `/jobs?company=:id`
  - Back → `/dashboard/employer`
- **Protected**: Yes (Employer only)

---

### 9. **Applications Page** (`/applications`)
- **Purpose**: View all user applications (Job Seeker)
- **Components**:
  - Application list/grid
  - Status badges (Applied, Reviewing, Rejected, Accepted, Withdrawn)
  - Filter by status
  - Applied date, job title, company
  - Withdraw application button (if status = Applied)
- **Navigation**:
  - Click application → `/application/:id`
  - Back → `/dashboard/seeker`
- **Protected**: Yes (Job Seeker only)

---

### 10. **Application Details Page** (`/application/:id`)
- **Purpose**: View single application details
- **Components**:
  - Application status timeline
  - Job details (summary)
  - Application date, resume used
  - Cover letter shown
  - Messages/feedback from employer (if any)
  - Withdraw button
- **Navigation**:
  - Back to applications → `/applications`
  - View job → `/jobs/:jobId`
- **Protected**: Yes (Job Seeker only)

---

### 11. **Applicants Management** (`/job/:id/applicants`)
- **Purpose**: View all applicants for a posted job (Employer)
- **Components**:
  - Applicant list with status
  - Filter by status
  - Search applicants
  - Bulk action options (Accept, Reject)
  - Click applicant → `/applicant/:id`
- **Navigation**:
  - Click applicant → `/applicant/:id`
  - Back → `/dashboard/employer`
- **Protected**: Yes (Employer only, owns the job)

---

### 12. **Applicant Details Page** (`/applicant/:id`)
- **Purpose**: View detailed applicant profile (Employer)
- **Components**:
  - Applicant's profile summary
  - Resume viewer/download
  - Application timeline
  - Change status (Accept/Reject/Shortlist)
  - Send message button
  - View job seeker profile link → `/job-seeker/:id`
- **Navigation**:
  - Back to applicants → `/job/:jobId/applicants`
  - View full profile → `/job-seeker/:id`
- **Protected**: Yes (Employer only, owns the job)

---

### 13. **Job Seeker Public Profile** (`/job-seeker/:id`)
- **Purpose**: View public job seeker profile (Employer viewing)
- **Components**:
  - Public profile info (Name, Location, Skills, Experience summary)
  - Resume (if shared publicly)
  - Education, experience highlights
  - Contact button (Send message)
  - Profile views count
- **Navigation**:
  - Send message → Message modal
  - Back → `/applicant/:id`
- **Protected**: Yes (Employer only)

---

### 14. **Saved Jobs Page** (`/saved-jobs`)
- **Purpose**: View saved/bookmarked jobs
- **Components**:
  - List of saved jobs
  - Remove from saved button
  - Quick apply button
  - Filter and sort options
- **Navigation**:
  - Click job → `/jobs/:id`
  - Click apply → `/jobs/apply/:id`
  - Back → `/dashboard/seeker`
- **Protected**: Yes (Job Seeker only)

---

## Navigation Structure Map

```
Public Routes:
├── /login
├── /register
├── /forgot-password
├── /jobs (listing only, no apply/save)
└── /jobs/:id (view only)

Seeker Protected Routes:
├── /dashboard/seeker
├── /profile
├── /jobs (with apply/save)
├── /jobs/:id (with apply/save)
├── /jobs/apply/:id
├── /applications
├── /application/:id
├── /saved-jobs
└── /job-seeker/:id (when viewed from employer)

Employer Protected Routes:
├── /dashboard/employer
├── /company-profile
├── /jobs/create
├── /job/:id/applicants
├── /applicant/:id
└── /job-seeker/:id
```

---

## Key Navigation Flows

### Job Seeker Flow:
Register/Login → Dashboard → Browse Jobs → View Job Details → Apply → Check Applications → View Application Status

### Employer Flow:
Register/Login → Dashboard → Post Job → View Applications → Review Applicants → View Applicant Profile