import { Routes, Route, Navigate } from 'react-router-dom';
import ProtectedRoute from '../components/common/ProtectedRoute';

// Auth Pages
import LoginPage from '../pages/auth/LoginPage';
import RegisterPage from '../pages/auth/RegisterPage';
import ForgotPasswordPage from '../pages/auth/ForgotPasswordPage';

// Job Seeker Pages
import SeekerDashboard from '../pages/seeker/SeekerDashboard';
import SeekerProfile from '../pages/seeker/SeekerProfile';
import ApplicationsList from '../pages/seeker/ApplicationsList';
import ApplicationDetails from '../pages/seeker/ApplicationDetails';
import SavedJobs from '../pages/seeker/SavedJobs';

// Employer Pages
import EmployerDashboard from '../pages/employer/EmployerDashboard';
import CompanyProfile from '../pages/employer/CompanyProfile';
import CreateJob from '../pages/employer/CreateJob';
import ApplicantsList from '../pages/employer/ApplicantsList';
import ApplicantDetails from '../pages/employer/ApplicantDetails';

// Job Pages
import JobsListing from '../pages/jobs/JobsListing';
import JobDetails from '../pages/jobs/JobDetails';
import JobApplication from '../pages/jobs/JobApplication';
import JobSeekerPublicProfile from '../pages/jobs/JobSeekerPublicProfile';

function Router() {
  return (
    <Routes>
      {/* Root redirect */}
      <Route path="/" element={<Navigate to="/login" />} />

      {/* Public Auth Routes */}
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/forgot-password" element={<ForgotPasswordPage />} />

      {/* Public Job Routes - View Only */}
      <Route path="/jobs" element={<JobsListing />} />
      <Route path="/jobs/:id" element={<JobDetails />} />

      {/* Job Seeker Protected Routes */}
      <Route
        path="/dashboard/seeker"
        element={
          <ProtectedRoute requiredRole="SEEKER">
            <SeekerDashboard />
          </ProtectedRoute>
        }
      />
      <Route
        path="/profile"
        element={
          <ProtectedRoute requiredRole="SEEKER">
            <SeekerProfile />
          </ProtectedRoute>
        }
      />
      <Route
        path="/jobs/apply/:id"
        element={
          <ProtectedRoute requiredRole="SEEKER">
            <JobApplication />
          </ProtectedRoute>
        }
      />
      <Route
        path="/applications"
        element={
          <ProtectedRoute requiredRole="SEEKER">
            <ApplicationsList />
          </ProtectedRoute>
        }
      />
      <Route
        path="/application/:id"
        element={
          <ProtectedRoute requiredRole="SEEKER">
            <ApplicationDetails />
          </ProtectedRoute>
        }
      />
      <Route
        path="/saved-jobs"
        element={
          <ProtectedRoute requiredRole="SEEKER">
            <SavedJobs />
          </ProtectedRoute>
        }
      />

      {/* Employer Protected Routes */}
      <Route
        path="/dashboard/employer"
        element={
          <ProtectedRoute requiredRole="EMPLOYER">
            <EmployerDashboard />
          </ProtectedRoute>
        }
      />
      <Route
        path="/company-profile"
        element={
          <ProtectedRoute requiredRole="EMPLOYER">
            <CompanyProfile />
          </ProtectedRoute>
        }
      />
      <Route
        path="/jobs/create"
        element={
          <ProtectedRoute requiredRole="EMPLOYER">
            <CreateJob />
          </ProtectedRoute>
        }
      />
      <Route
        path="/job/:id/applicants"
        element={
          <ProtectedRoute requiredRole="EMPLOYER">
            <ApplicantsList />
          </ProtectedRoute>
        }
      />
      <Route
        path="/applicant/:id"
        element={
          <ProtectedRoute requiredRole="EMPLOYER">
            <ApplicantDetails />
          </ProtectedRoute>
        }
      />
      <Route
        path="/job-seeker/:id"
        element={
          <ProtectedRoute requiredRole="EMPLOYER">
            <JobSeekerPublicProfile />
          </ProtectedRoute>
        }
      />

      {/* 404 Catch-all */}
      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  );
}

export default Router;
