import apiClient from './apiClient';

export const applicationService = {
  submitApplication: async (email, jobId, payload) => {
    const response = await apiClient.post(`/api/applications/apply/${email}/${jobId}`, payload);
    return response?.data?.data;
  },

  getMyApplications: async (email) => {
    const response = await apiClient.get(email ? `/api/applications/my/${email}` : '/api/applications/my');
    return response?.data?.data || [];
  },

  getApplicationById: async (applicationId) => {
    const response = await apiClient.get(`/api/applications/${applicationId}`);
    return response?.data?.data;
  },
  
  updateApplicationStatus: async (applicationId, status) => {
    const response = await apiClient.put(`/api/applications/${applicationId}/status`, null, {
      params: { status }
    });
    return response?.data?.data;
  },
  
  getJobApplications: async (jobId) => {
    const response = await apiClient.get(`/api/applications/job/${jobId}`);
    return response?.data?.data || [];
  },
};
