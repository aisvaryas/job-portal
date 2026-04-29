import apiClient from './apiClient';

const getUser = () => JSON.parse(localStorage.getItem('user') || '{}');

export const profileService = {
  // Get seeker profile by email
  getSeekerProfile: async (email) => {
    const response = await apiClient.get(email ? `/api/jobseeker/profile/${email}` : '/api/jobseeker/profile');
    return response?.data?.data;
  },

  getCurrentSeekerProfile: async () => {
    const response = await apiClient.get('/api/jobseeker/profile');
    return response?.data?.data;
  },

  updateCurrentSeekerProfile: async (payload) => {
    const response = await apiClient.put('/api/jobseeker/profile', payload);
    return response?.data?.data;
  },

  getCurrentEmployerProfile: async () => {
    const response = await apiClient.get('/api/employer/profile');
    return response?.data?.data;
  },

  // Get seeker by user ID
  getSeekerByUserId: async (userId) => {
    const response = await apiClient.get('/api/jobseeker/all');
    const all = response?.data?.data || [];
    return all.find((item) => item?.user?.id === userId);
  },

  getEmployerByUserId: async (userId) => {
    const response = await apiClient.get('/api/employer/all');
    const all = response?.data?.data || [];
    return all.find((item) => item?.user?.id === userId);
  },

  ensureEmployerProfile: async (user) => {
    if (!user?.id) {
      throw new Error('User session not found');
    }

    const existing = await profileService.getEmployerByUserId(user.id);
    if (existing?.id) {
      return existing;
    }

    const emailPrefix = (user.email || 'employer').split('@')[0];
    const names = emailPrefix.split(/[._-]+/).filter(Boolean);
    const contactFirstName = names[0] || 'Employer';
    const contactLastName = names.slice(1).join(' ') || 'User';

    await apiClient.post('/api/employer/save', {
      userId: user.id,
      contactFirstName,
      contactLastName,
      companyName: `${contactFirstName} Company`,
      industry: '',
      website: '',
    });

    const created = await profileService.getEmployerByUserId(user.id);
    if (!created?.id) {
      throw new Error('Employer profile creation failed');
    }
    return created;
  },

  getSeekerProfileBundle: async () => {
    const user = getUser();
    const seeker = await profileService.getCurrentSeekerProfile();
    const email = user.email;
    const [skillsRes, expRes, eduRes] = await Promise.all([
      apiClient.get(`/seekerskill/all/${email}`).catch((err) => {
        console.error('Skills fetch error:', err);
        return { data: { data: [] } };
      }),
      apiClient.get(`/experience/all/${email}`).catch((err) => {
        console.error('Experience fetch error:', err);
        return { data: { data: [] } };
      }),
      apiClient.get(`/education/all/${email}`).catch((err) => {
        console.error('Education fetch error:', err);
        return { data: { data: [] } };
      }),
    ]);
    return {
      seeker,
      skills: skillsRes?.data?.data || [],
      experience: expRes?.data?.data || [],
      education: eduRes?.data?.data || [],
      email,
    };
  },

  addSkill: async (email, skillName) => {
    const response = await apiClient.post(`/seekerskill/save/${email}`, { skillName });
    return response?.data?.data;
  },

  updateSkill: async (email, oldSkillName, skillName) => {
    const response = await apiClient.put(`/seekerskill/update/${email}/${encodeURIComponent(oldSkillName)}`, { skillName });
    return response?.data?.data;
  },

  addExperience: async (email, payload) => {
    // Convert date strings to ISO format if needed
    const formattedPayload = {
      ...payload,
      startDate: payload.startDate ? new Date(payload.startDate).toISOString().split('T')[0] : null,
      endDate: payload.endDate ? new Date(payload.endDate).toISOString().split('T')[0] : null,
    };
    const response = await apiClient.post(`/experience/save/${email}`, formattedPayload);
    return response?.data?.data;
  },

  addEducation: async (email, payload) => {
    // Convert date strings to ISO format if needed
    const formattedPayload = {
      ...payload,
      graduationDate: payload.graduationDate ? new Date(payload.graduationDate).toISOString().split('T')[0] : null,
    };
    const response = await apiClient.post(`/education/save/${email}`, formattedPayload);
    return response?.data?.data;
  },

  updateExperience: async (email, id, payload) => {
    const formattedPayload = {
      ...payload,
      startDate: payload.startDate ? new Date(payload.startDate).toISOString().split('T')[0] : null,
      endDate: payload.endDate ? new Date(payload.endDate).toISOString().split('T')[0] : null,
    };
    const response = await apiClient.put(`/experience/update/${email}/${id}`, formattedPayload);
    return response?.data?.data;
  },

  updateEducation: async (email, id, payload) => {
    const formattedPayload = {
      ...payload,
      graduationDate: payload.graduationDate ? new Date(payload.graduationDate).toISOString().split('T')[0] : null,
    };
    const response = await apiClient.put(`/education/update/${email}/${id}`, formattedPayload);
    return response?.data?.data;
  },

  deleteExperience: async (email, id) => {
    const response = await apiClient.delete(`/experience/delete/${email}/${id}`);
    return response?.data?.data;
  },

  deleteEducation: async (email, id) => {
    const response = await apiClient.delete(`/education/delete/${email}/${id}`);
    return response?.data?.data;
  },

  deleteSkill: async (email, skillName) => {
    const response = await apiClient.delete(`/seekerskill/delete/${email}/${encodeURIComponent(skillName)}`);
    return response?.data?.data;
  },
};
