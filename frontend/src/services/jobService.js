import apiClient from './apiClient';

export const jobService = {
  getAllJobs: async (filters = {}) => {
    const hasSearch = filters?.location || filters?.title || filters?.experience;
    const response = hasSearch
      ? await apiClient.get('/api/jobs', {
          params: { 
            location: filters.location, 
            title: filters.title || filters.search,
            experience: filters.experience
          },
        })
      : await apiClient.get('/api/jobs');
    return response?.data?.data || [];
  },

  getJobById: async (id) => {
    const response = await apiClient.get(`/api/jobs/${id}`);
    return response?.data?.data;
  },

  createJob: async (employerId, jobData) => {
    const response = await apiClient.post(`/api/employer/${employerId}/jobs`, jobData);
    return response?.data?.data;
  },

  getEmployerJobs: async (employerId) => {
    const response = await apiClient.get(`/api/employer/${employerId}/jobs`);
    return response?.data?.data || [];
  },
  
  getApplicationsByJob: async (jobId) => {
    const response = await apiClient.get(`/api/employer/jobs/${jobId}/applicants`);
    return response?.data?.data || [];
  },
  
  getApplicationCount: async (jobId) => {
    const response = await apiClient.get(`/api/employer/jobs/${jobId}/applicants/count`);
    return response?.data?.data || 0;
  },
  
  updateJob: async (employerId, jobId, jobData) => {
    const response = await apiClient.put(`/api/employer/${employerId}/jobs/${jobId}`, jobData);
    return response?.data?.data;
  },
  
  closeJob: async (jobId) => {
    const response = await apiClient.put(`/api/employer/jobs/close/${jobId}`);
    return response?.data?.data;
  },
  
  reopenJob: async (jobId) => {
    const response = await apiClient.put(`/api/employer/jobs/reopen/${jobId}`);
    return response?.data?.data;
  },
  
  deleteJob: async (jobId) => {
    const response = await apiClient.delete(`/api/employer/jobs/delete/${jobId}`);
    return response?.data?.data;
  },
};
