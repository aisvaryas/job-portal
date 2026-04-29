// API Endpoints
export const API_ENDPOINTS = {
  // Auth
  REGISTER: '/auth/register',
  LOGIN: '/auth/login',
  LOGOUT: '/auth/logout',
  PROFILE: '/auth/profile',
  FORGOT_PASSWORD: '/auth/forgot-password',
  VERIFY_OTP: '/auth/verify-otp',
  RESET_PASSWORD: '/auth/reset-password',

  // Jobs
  JOBS: '/jobs',
  JOB_DETAILS: '/jobs/:id',
  APPLY_JOB: '/jobs/:id/apply',
  SAVE_JOB: '/jobs/:id/save',
  REMOVE_SAVED_JOB: '/jobs/:id/save',
  SAVED_JOBS: '/jobs/saved',
  CREATE_JOB: '/jobs',

  // Applications
  APPLICATIONS: '/applications',
  APPLICATION_DETAILS: '/applications/:id',
  WITHDRAW_APPLICATION: '/applications/:id/withdraw',
  UPDATE_APPLICATION_STATUS: '/applications/:id/status',
  JOB_APPLICATIONS: '/jobs/:id/applications',

  // Company
  COMPANY_PROFILE: '/company/profile',
  UPLOAD_COMPANY_LOGO: '/company/upload-logo',
  COMPANY_STATS: '/company/stats',

  // Applicants
  APPLICANT_PROFILE: '/applicants/:id',
  APPLICANTS: '/applicants',
};

// User Roles
export const USER_ROLES = {
  SEEKER: 'SEEKER',
  EMPLOYER: 'EMPLOYER',
};

// Application Status
export const APPLICATION_STATUS = {
  APPLIED: 'Applied',
  REVIEWING: 'Reviewing',
  SHORTLISTED: 'Shortlisted',
  INTERVIEW: 'Interview',
  ACCEPTED: 'Accepted',
  REJECTED: 'Rejected',
  WITHDRAWN: 'Withdrawn',
};

// Job Status
export const JOB_STATUS = {
  ACTIVE: 'Active',
  CLOSED: 'Closed',
  DRAFT: 'Draft',
};

// Job Types
export const JOB_TYPES = {
  FULL_TIME: 'Full-time',
  PART_TIME: 'Part-time',
  CONTRACT: 'Contract',
  INTERNSHIP: 'Internship',
  FREELANCE: 'Freelance',
};

// Experience Levels
export const EXPERIENCE_LEVELS = {
  ENTRY: 'Entry Level',
  MID: 'Mid Level',
  SENIOR: 'Senior Level',
  EXECUTIVE: 'Executive',
};

// Company Sizes
export const COMPANY_SIZES = {
  STARTUP: '1-50',
  SMALL: '51-200',
  MEDIUM: '201-1000',
  LARGE: '1000+',
};

// Error Messages
export const ERROR_MESSAGES = {
  NETWORK_ERROR: 'Network error. Please check your connection.',
  INVALID_EMAIL: 'Invalid email address.',
  WEAK_PASSWORD: 'Password must be at least 8 characters.',
  PASSWORD_MISMATCH: 'Passwords do not match.',
  REQUIRED_FIELD: 'This field is required.',
  UNAUTHORIZED: 'You are not authorized to access this resource.',
  NOT_FOUND: 'Resource not found.',
  SERVER_ERROR: 'Something went wrong. Please try again later.',
};

// Success Messages
export const SUCCESS_MESSAGES = {
  LOGIN_SUCCESS: 'Logged in successfully.',
  REGISTER_SUCCESS: 'Account created successfully.',
  PROFILE_UPDATED: 'Profile updated successfully.',
  JOB_POSTED: 'Job posted successfully.',
  APPLICATION_SUBMITTED: 'Application submitted successfully.',
  PASSWORD_RESET: 'Password reset successfully.',
};

// Pagination
export const PAGINATION = {
  DEFAULT_PAGE_SIZE: 10,
  DEFAULT_PAGE: 1,
};

// Filter Options
export const FILTER_OPTIONS = {
  EXPERIENCE_LEVELS: Object.values(EXPERIENCE_LEVELS),
  JOB_TYPES: Object.values(JOB_TYPES),
  COMPANY_SIZES: Object.values(COMPANY_SIZES),
};

// Local Storage Keys
export const STORAGE_KEYS = {
  AUTH_TOKEN: 'authToken',
  USER_ROLE: 'userRole',
  USER_DATA: 'user',
  PREFERENCES: 'preferences',
};
