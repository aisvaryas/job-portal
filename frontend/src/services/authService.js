import apiClient from './apiClient';

const mapRoleToBackend = (role) => (role === 'SEEKER' ? 'JOB_SEEKER' : role);
const mapRoleToFrontend = (role) => (role === 'JOB_SEEKER' ? 'SEEKER' : role);

const saveAuth = (token, role, user) => {
  localStorage.setItem('authToken', token);
  localStorage.setItem('userRole', mapRoleToFrontend(role));
  localStorage.setItem('user', JSON.stringify(user));
};

export const authService = {
  register: async (userData) => {
    const role = mapRoleToBackend(userData.userType);
    const registerRes = await apiClient.post('/users/register', {
      email: userData.email,
      password: userData.password,
      role,
    });
    return registerRes?.data;
  },

  login: async (email, password) => {
    try {
      const response = await apiClient.post('/auth/login', { email, password });
  
      const payload = response?.data?.data;
  
      if (!payload) {
        throw new Error('Invalid login response');
      }
  
      const user = {
        id: payload.userId,
        email: payload.email,
        role: mapRoleToFrontend(payload.role),
      };
  
      saveAuth(payload.token, payload.role, user);
  
      return {
        token: payload.token,
        userRole: user.role,
        user,
      };
    } catch (error) {
      throw new Error(
        error?.response?.data?.message || 'Login failed'
      );
    }
  },

  logout: () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userRole');
    localStorage.removeItem('user');
  },

  getProfile: async () => JSON.parse(localStorage.getItem('user')),
};