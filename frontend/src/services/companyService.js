import apiClient from './apiClient';

export const companyService = {
  getCompanyProfile: async () => {
    const response = await apiClient.get('/api/employer/profile');
    return response?.data?.data;
  },

  updateCompanyProfile: async (profileData) => {
    const response = await apiClient.put('/api/employer/profile', profileData);
    return response?.data?.data;
  },
};
