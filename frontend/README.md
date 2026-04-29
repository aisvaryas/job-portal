# JobPortal - Job Portal Frontend

A modern, responsive React + Vite frontend for a comprehensive job portal application with separate dashboards and flows for job seekers and employers.

## Project Structure

```
frontend/
├── src/
│   ├── components/
│   │   ├── common/              # Shared components
│   │   │   ├── Header.jsx
│   │   │   ├── Navbar.jsx
│   │   │   ├── Footer.jsx
│   │   │   └── ProtectedRoute.jsx
│   │   ├── auth/                # Auth components
│   │   ├── seeker/              # Job seeker components
│   │   ├── employer/            # Employer components
│   │   └── jobs/                # Job-related components
│   ├── pages/
│   │   ├── auth/                # Authentication pages
│   │   │   ├── LoginPage.jsx
│   │   │   ├── RegisterPage.jsx
│   │   │   └── ForgotPasswordPage.jsx
│   │   ├── seeker/              # Job seeker pages
│   │   │   ├── SeekerDashboard.jsx
│   │   │   ├── SeekerProfile.jsx
│   │   │   ├── ApplicationsList.jsx
│   │   │   ├── ApplicationDetails.jsx
│   │   │   └── SavedJobs.jsx
│   │   ├── employer/            # Employer pages
│   │   │   ├── EmployerDashboard.jsx
│   │   │   ├── CompanyProfile.jsx
│   │   │   ├── CreateJob.jsx
│   │   │   ├── ApplicantsList.jsx
│   │   │   └── ApplicantDetails.jsx
│   │   └── jobs/                # Job listing pages
│   │       ├── JobsListing.jsx
│   │       ├── JobDetails.jsx
│   │       ├── JobApplication.jsx
│   │       └── JobSeekerPublicProfile.jsx
│   ├── hooks/
│   │   └── useAuth.js           # Authentication hook
│   ├── services/
│   │   ├── apiClient.js         # Axios instance
│   │   ├── authService.js       # Auth API calls
│   │   ├── jobService.js        # Job API calls
│   │   ├── applicationService.js # Application API calls
│   │   └── companyService.js    # Company API calls
│   ├── utils/                   # Utility functions
│   ├── styles/
│   │   ├── global.css           # Global styles
│   │   ├── Navbar.css
│   │   ├── Header.css
│   │   ├── Footer.css
│   │   ├── Auth.css
│   │   ├── Dashboard.css
│   │   ├── JobsListing.css
│   │   ├── Profile.css
│   │   ├── Applications.css
│   │   └── ... (other page styles)
│   ├── router/
│   │   └── Router.jsx           # Route configuration
│   ├── App.jsx                  # App component
│   └── main.jsx                 # Entry point
├── public/
│   └── index.html
├── package.json
├── vite.config.js
├── .env.example
└── README.md
```

## Features

### Authentication
- Login/Register for Job Seekers and Employers
- Forgot Password flow
- JWT-based authentication
- Protected routes

### Job Seeker Features
- **Dashboard**: View stats, featured jobs, recommendations
- **Browse Jobs**: Search, filter, and apply for jobs
- **Profile Management**: Update personal info, upload resume, manage skills
- **Applications**: Track application status and view details
- **Saved Jobs**: Bookmark favorite jobs
- **Public Profile**: View by employers

### Employer Features
- **Dashboard**: View posted jobs, applications, company stats
- **Post Jobs**: Create and manage job listings
- **Company Profile**: Update company information and logo
- **Applications**: Review and manage applicants
- **Applicant Profiles**: View detailed profiles of job seekers

## Setup

### Prerequisites
- Node.js 16+
- npm or yarn

### Installation

1. **Clone and Navigate**
   ```bash
   cd frontend
   ```

2. **Install Dependencies**
   ```bash
   npm install
   ```

3. **Create Environment File**
   ```bash
   cp .env.example .env
   ```

4. **Update `.env` with your backend API URL**
   ```
   VITE_API_URL=http://localhost:8080/api
   VITE_APP_NAME=JobPortal
   ```

### Development

**Start development server**
```bash
npm run dev
```

The app will open at `http://localhost:3000`

### Build

**Create production build**
```bash
npm run build
```

**Preview production build**
```bash
npm run preview
```

## Routing

### Public Routes
- `/login` - User login
- `/register` - User registration
- `/forgot-password` - Password recovery
- `/jobs` - Browse all jobs (view only)
- `/jobs/:id` - Job details (view only)

### Job Seeker Routes (Protected)
- `/dashboard/seeker` - Dashboard
- `/profile` - Profile management
- `/jobs` - Browse jobs (with apply/save)
- `/jobs/:id` - Job details (with apply/save)
- `/jobs/apply/:id` - Apply for job
- `/applications` - View applications
- `/application/:id` - Application details
- `/saved-jobs` - Saved jobs

### Employer Routes (Protected)
- `/dashboard/employer` - Dashboard
- `/company-profile` - Company profile
- `/jobs/create` - Post new job
- `/job/:id/applicants` - View job applicants
- `/applicant/:id` - Applicant details
- `/job-seeker/:id` - View job seeker profile

## API Integration

All API calls are made through service files in `src/services/`:

- **authService.js** - Authentication endpoints
- **jobService.js** - Job management endpoints
- **applicationService.js** - Application endpoints
- **companyService.js** - Company profile endpoints

### Authentication
Tokens are automatically added to request headers via Axios interceptors in `apiClient.js`.

## Styling

- **Global Styles**: `src/styles/global.css`
- **Component Styles**: Individual CSS files in `src/styles/`
- **CSS Variables**: Color scheme defined in `:root`
- **Responsive Design**: Mobile-first approach with media queries

## Technologies

- **React 18** - UI library
- **Vite** - Build tool
- **React Router DOM** - Client-side routing
- **Axios** - HTTP client
- **CSS3** - Styling

## Best Practices

1. **Components**: Functional components with hooks
2. **Services**: Centralized API calls
3. **Hooks**: Custom hooks for shared logic
4. **Styling**: Component-scoped CSS files
5. **Protected Routes**: Role-based access control
6. **Error Handling**: Try-catch and error messages
7. **Responsive**: Mobile-first design

## Environment Variables

```
VITE_API_URL      - Backend API base URL
VITE_APP_NAME     - Application name
```

## Notes

- Ensure backend API is running on the configured port
- User roles are `SEEKER` or `EMPLOYER`
- Auth token is stored in localStorage
- CORS should be configured on backend for frontend URL

## Future Enhancements

- Dark mode support
- Advanced job filtering
- Messaging system
- Video interviews
- Advanced analytics
- Email notifications
- Mobile app version

## Support

For issues or questions, please contact the development team.
